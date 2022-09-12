package cn.xpp011.dingrobot.ratelimiter;

import cn.xpp011.dingrobot.excepation.InternalErrorException;
import cn.xpp011.dingrobot.executor.TaskEnforcer;
import cn.xpp011.dingrobot.executor.TaskParams;

import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @program: ding-robot
 * @description: 滑动窗口限流器
 * @author: xpp011
 * @create: 2022-09-05 22:21
 **/

public class SimpleSlidingWindowRateLimiter implements RateLimiter {

    private final TaskEnforcer<TaskParams> taskEnforcer;

    private static final long TRY_LOCK_TIMEOUT = 500L;

    private final int limit;

    private final long windowSize;

    /**
     * 剩余令牌因子
     * remain = limit * remainFactor
     */
    private float remainFactor;

    private Lock lock = new ReentrantLock();

    private Deque<Long> queue = new LinkedList<>();

    public SimpleSlidingWindowRateLimiter(TaskEnforcer<TaskParams> taskEnforcer, int limit, long windowSize) {
        this(taskEnforcer, limit, windowSize, REMAIN_FACTOR);
    }

    public SimpleSlidingWindowRateLimiter(TaskEnforcer<TaskParams> taskEnforcer, int limit, long windowSize, float remainFactor) {
        this.taskEnforcer = taskEnforcer;
        this.limit = limit;
        this.windowSize = windowSize;
        this.remainFactor = remainFactor;
    }

    @Override
    public boolean tryAcquire() throws InternalErrorException {
        return tryAcquire(0L);
    }

    @Override
    public boolean tryAcquireRemain() throws InternalErrorException {
        return tryAcquire((long) (limit * remainFactor));
    }

    public boolean tryAcquire(long tryAcquireRemain) throws InternalErrorException {
        try {
            if (lock.tryLock(TRY_LOCK_TIMEOUT, TimeUnit.MILLISECONDS)) {
                long now = System.currentTimeMillis() / 1000;
                long floor = now - windowSize;
                while (!queue.isEmpty() && queue.peek() < floor) queue.poll();
                int size = queue.size();
                if (size < limit && limit - size > tryAcquireRemain) {
                    return queue.offer(now);
                }
            }
        } catch (InterruptedException e) {
            throw new InternalErrorException("tryAcquire() is interrupted by lock-time-out.", e);
        } finally {
            lock.unlock();
        }
        return false;
    }
}
