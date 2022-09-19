package cn.xpp011.dingrobot;

import cn.xpp011.dingrobot.excepation.AcquireTokenException;
import cn.xpp011.dingrobot.message.FailMessage;
import cn.xpp011.dingrobot.ratelimiter.RateLimiter;
import cn.xpp011.dingrobot.ratelimiter.RateLimiterFactory;
import cn.xpp011.dingrobot.storage.FailMessageQueue;
import cn.xpp011.dingrobot.storage.SimpleFailMessageQueue;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 默认的失败消息处理器
 *
 * @author: xpp011 2022-08-29 23:07
 **/

public class DefaultFailMessageHandler implements FailMessageHandler {

    private static final Logger log = LoggerFactory.getLogger(DefaultFailMessageHandler.class);

    private final static AtomicBoolean running = new AtomicBoolean(false);

    private final FailMessageQueue queue;

    private final DingRobotFactory dingRobotFactory;

    private final ScheduledExecutorService scheduledExecutorService = createDefaultScheduledExecutorService();

    private final int retry;

    public DefaultFailMessageHandler(FailMessageQueue queue, DingRobotFactory dingRobotFactory, int retry) {
        this.queue = queue;
        this.dingRobotFactory = dingRobotFactory;
        this.retry = retry;
    }

    @Override
    public boolean handleMessage(FailMessage failMessage) throws Exception {
        Assert.notNull(failMessage, "failMessage argument is required");
        DingRobotTemplate dingRobot = dingRobotFactory.getDingRobot(failMessage.getRobotName());
        boolean success = false;
        try {
            RateLimiter rateLimiter = RateLimiterFactory.getRateLimiter(failMessage.getRobotName());
            if (rateLimiter == null) {
                log.error("未找到robot: {}的限流器", failMessage.getRobotName());
                throw new AcquireTokenException("no limiter found for robot " + failMessage.getRobotName());
            }
            if (rateLimiter.tryAcquireRemain()) {
                success = dingRobot.doSend(failMessage.getMessage(), retry);
            } else {
                log.info("消息重试尝试获取令牌失败dingRobot: {}", failMessage.getRobotName());
            }
        } catch (Exception e) {
            log.error("失败消息重试处理失败： {}", e.getMessage(), e);
            throw e;
        }
        return success;
    }

    @Override
    public FailMessage getFailMessage() {
        return queue.pop();
    }

    @Override
    public void start() {
        if (running.compareAndSet(false, true)) {
            //不可以向scheduledExecutorService抛出未捕获异常，这会导致后续任务停止执行
            scheduledExecutorService.scheduleAtFixedRate(() -> {
                FailMessage failMessage;
                try {
                    failMessage = getFailMessage();
                    if (failMessage == null) return;
                } catch (Exception e) {
                    log.error("获取失败消息队列异常: {}", e.getMessage(), e);
                    return;
                }
                try {
                    log.info("process fail message task running....");
                    boolean success = handleMessage(failMessage);
                    log.info("process fail message task ending...., result: {}", success);
                    if (!success) queue.push(failMessage);
                } catch (Exception e) {
                    if (ExceptionUtil.isNetworkException(e) && failMessage.increment() <= retry) {
                        queue.push(failMessage);
                    } else {
                        log.error("处理发送失败消息失败: {}, message: {}", e.getMessage(), failMessage);
                    }
                }
            }, RateLimiterFactory.getInitialDelay(), RateLimiterFactory.getPeriod(), RateLimiterFactory.getUnit());
        }
    }

    @Override
    public void close() {
        if (running.compareAndSet(true, false)) {
            scheduledExecutorService.shutdown();
            try {
                //等待任务执行完成
                scheduledExecutorService.awaitTermination(5, TimeUnit.SECONDS);
                if (queue instanceof SimpleFailMessageQueue) {
                    //本地失败消息打印
                    while (!queue.isEmpty()) {
                        log.error("fail message queue message: {}", queue.pop());
                    }
                }
            } catch (InterruptedException e) {
            }
        }
    }

    @Override
    public boolean isRunning() {
        return running.get();
    }

    private ScheduledExecutorService createDefaultScheduledExecutorService() {
        ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat("ProcessFailMessageTask-Thread-%d")
                .build();
        return new ScheduledThreadPoolExecutor(1, threadFactory);
    }
}
