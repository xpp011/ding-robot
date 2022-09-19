package cn.xpp011.dingrobot.storage;

import cn.xpp011.dingrobot.ExceptionUtil;
import cn.xpp011.dingrobot.message.FailMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * redis失败队列
 *
 * @author: xpp011 2022-08-21 21:13
 **/

public class RedisFailMessageQueue implements FailMessageQueue {

    private static final Logger log = LoggerFactory.getLogger(RedisFailMessageQueue.class);
    private static String key = "ding-robot:fm-que";

    private final RedisTemplate redisTemplate;

    private final int retry;

    public RedisFailMessageQueue(RedisTemplate redisTemplate, int retry) {
        this.redisTemplate = redisTemplate;
        this.retry = retry;
    }

    public RedisFailMessageQueue(RedisTemplate redisTemplate, String queueKey, int retry) {
        this.redisTemplate = redisTemplate;
        this.key = queueKey;
        this.retry = retry;
    }

    @Override
    public boolean push(FailMessage message) {
        return push(message, retry);
    }

    private boolean push(FailMessage message, int retry) {
        try {
            Long res = redisTemplate.opsForList().rightPush(key, message);
            return res >= 1L;
        } catch (Exception e) {
            if (ExceptionUtil.isNetworkException(e) && retry > 0) return push(message, --retry);
            log.error("push failMessageQueue fail: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public FailMessage pop() {
        return pop(retry);
    }

    private FailMessage pop(int retry) {
        try {
            FailMessage failMessage = (FailMessage) redisTemplate.opsForList().leftPop(key);
            return failMessage;
        } catch (Exception e) {
            if (ExceptionUtil.isNetworkException(e) && retry > 0) return pop(--retry);
            log.error("pop failMessageQueue fail: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public long size() {
        return redisTemplate.opsForList().size(key);
    }

    @Override
    public boolean isEmpty() {
        return size() == 0L;
    }
}

