package cn.xpp011.dingrobot.executor;

/**
 * @program: ding-robot
 * @description: redis任务执行参数
 * @author: xpp011
 * @create: 2022-08-20 23:50
 **/

public class RedisTaskParams implements TaskParams {

    /**
     * 机器人名称
     */
    private String key;

    /**
     * 限制令牌大小
     */
    private long limit;

    /**
     * 限流滑动窗口大小(时间区间)
     */
    private long windowSize;

    /**
     * 脚本地址
     */
    private String scriptPath;

    /**
     * 尝试获取令牌的界限(避免定时任务抢夺正常消息的令牌，影响正常消息的实时性)
     */
    private long tryAcquireRemain;

    public RedisTaskParams(String key, int limit, long windowSize, String scriptPath, long tryAcquireRemain) {
        this.key = key;
        this.limit = limit;
        this.windowSize = windowSize;
        this.scriptPath = scriptPath;
        this.tryAcquireRemain = tryAcquireRemain;
    }

    public String getKey() {
        return key;
    }

    public long getLimit() {
        return limit;
    }

    public long getWindowSize() {
        return windowSize;
    }

    public String getScriptPath() {
        return scriptPath;
    }

    public long getTryAcquireRemain() {
        return tryAcquireRemain;
    }
}
