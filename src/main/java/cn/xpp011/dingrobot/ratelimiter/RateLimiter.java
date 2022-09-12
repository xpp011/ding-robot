package cn.xpp011.dingrobot.ratelimiter;

import cn.xpp011.dingrobot.excepation.InternalErrorException;

public interface RateLimiter {

    /**
     * 剩余令牌因子
     */
    float REMAIN_FACTOR = 0.33f;

    /**
     * 尝试获取令牌
     *
     * @return 是否获取成功
     */
    boolean tryAcquire() throws InternalErrorException;

    /**
     * 尝试获取令牌(只有剩余令牌超过界限后才会获取令牌)
     *
     * @return
     */
    boolean tryAcquireRemain() throws InternalErrorException;

}
