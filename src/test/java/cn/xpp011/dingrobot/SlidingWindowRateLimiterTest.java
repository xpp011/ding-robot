package cn.xpp011.dingrobot;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @program: ding-robot
 * @description: 分布式限流测试
 * @author: xpp011
 * @create: 2022-08-28 22:51
 **/
@SpringBootTest(classes = DingRobotApplication.class)
public class SlidingWindowRateLimiterTest {

    private static final Logger log = LoggerFactory.getLogger(SlidingWindowRateLimiterTest.class);

    @Autowired
    private DingRobotFactory dingRobotFactory;

    @Test
    public void limitTest() throws ExecutionException, InterruptedException {
        DingRobotTemplate dingRobot = dingRobotFactory.getDingRobot("message-fail");
        ExecutorService executorService = Executors.newCachedThreadPool();
        ArrayList<Future> list = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            int j = i + 1;
            Future<Boolean> submit = executorService.submit(() -> dingRobot.send(MessageTest.builderTextMessage(j)));
            list.add(submit);
        }
        int i = 0;
        for (Future future : list) {
            log.info(future.get() + " : " + ++i);
        }
    }

    @Test
    public void limitTestAsync() throws ExecutionException, InterruptedException {
        DingRobotTemplate dingRobot = dingRobotFactory.getDingRobot("message-fail");
        ArrayList<Future> list = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            int j = i + 1;
            Future<Boolean> future = dingRobot.sendAsync(MessageTest.builderTextMessage(j));
            list.add(future);
        }
        int i = 0;
        for (Future future : list) {
            log.info(future.get() + " : " + ++i);
        }
    }


}
