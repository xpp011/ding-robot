package cn.xpp011.dingrobot.ratelimiter;

import cn.xpp011.dingrobot.excepation.InternalErrorException;
import cn.xpp011.dingrobot.executor.RedisTaskParams;
import cn.xpp011.dingrobot.executor.TaskEnforcer;
import cn.xpp011.dingrobot.executor.TaskParams;

/**
 * 滑动窗口限流器
 *
 * @author: xpp011 2022-08-18 22:49
 **/

public class DistributedSlidingWindowRateLimiter implements RateLimiter {

    private static final String SCRIPT_PATH = "lua/SlidingWindowLimiter.lua";

    private final TaskEnforcer<TaskParams> taskEnforcer;

    private final String key;

    private final int limit;

    private final long windowSize;

    /**
     * 剩余令牌因子
     * remain = limit * remainFactor
     */
    private float remainFactor;


    public DistributedSlidingWindowRateLimiter(TaskEnforcer<TaskParams> taskEnforcer, String key, int limit, long windowSize) {
        this(taskEnforcer, key, limit, windowSize, REMAIN_FACTOR);
    }


    public DistributedSlidingWindowRateLimiter(TaskEnforcer<TaskParams> taskEnforcer, String key, int limit, long windowSize, float remainFactor) {
        this.taskEnforcer = taskEnforcer;
        this.key = key;
        this.limit = limit;
        this.windowSize = windowSize;
        this.remainFactor = remainFactor;
    }

    @Override
    public boolean tryAcquire() throws InternalErrorException {
        return tryAcquire(0);
    }

    @Override
    public boolean tryAcquireRemain() throws InternalErrorException {
        return tryAcquire((long) (limit * remainFactor));
    }

    private boolean tryAcquire(long tryAcquireRemain) {
        TaskParams params = new RedisTaskParams(key, limit, windowSize, SCRIPT_PATH, tryAcquireRemain);
        Long result = taskEnforcer.execute(params);
        return result >= 1L;
    }
}
