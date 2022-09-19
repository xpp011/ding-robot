package cn.xpp011.dingrobot.ratelimiter;

import cn.xpp011.dingrobot.executor.RedisTaskEnforcer;
import cn.xpp011.dingrobot.executor.SimpleTaskEnforcer;
import cn.xpp011.dingrobot.executor.TaskEnforcer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * RateLimiter工厂类
 *
 * @author: xpp011 2022-08-21 23:29
 **/

public class RateLimiterFactory {

    private final static int LIMIT = 20;

    private final static long WINDOW_SIZE = 60;

    private final static int OFFSET = 300;

    private final static Map<String, RateLimiter> rateLimiterMap = new ConcurrentHashMap<>();

    private RateLimiterFactory() {
    }

    public static RateLimiter getRateLimiter(RateLimiterType type, TaskEnforcer taskEnforcer, String robotName) {
        RateLimiter rateLimiter = rateLimiterMap.getOrDefault(robotName, null);
        if (rateLimiter != null) return rateLimiter;
        if (RateLimiterType.SLIDING_WINDOW.equals(type)) {
            if (taskEnforcer instanceof RedisTaskEnforcer) {
                rateLimiter = new DistributedSlidingWindowRateLimiter(taskEnforcer, robotName, LIMIT, WINDOW_SIZE);
            }
            if (taskEnforcer instanceof SimpleTaskEnforcer) {
                rateLimiter = new SimpleSlidingWindowRateLimiter(taskEnforcer, LIMIT, WINDOW_SIZE);
            }
        }
        rateLimiterMap.put(robotName, rateLimiter);
        return rateLimiter;
    }

    public static RateLimiter getRateLimiter(String robotName) {
        return rateLimiterMap.getOrDefault(robotName, null);
    }

    /**
     * 定时任务执行周期
     *
     * @return 执行周期时间
     */
    public static long getPeriod() {
        return (WINDOW_SIZE / LIMIT) * 1000 * 2 + OFFSET;
    }

    /**
     * 定时任务执行周期时间单位
     *
     * @return 执行周期时间单位
     */
    public static TimeUnit getUnit() {
        return TimeUnit.MILLISECONDS;
    }

    /**
     * 定时任务初始延迟时间
     *
     * @return 初始延迟时间
     */
    public static long getInitialDelay() {
        return 2000L;
    }

}
