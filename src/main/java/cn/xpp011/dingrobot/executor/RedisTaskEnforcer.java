package cn.xpp011.dingrobot.executor;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

import java.util.Arrays;

/**
 * @program: ding-robot
 * @description: redis执行器
 * @author: xpp011
 * @create: 2022-08-18 22:46
 **/

public class RedisTaskEnforcer implements TaskEnforcer<RedisTaskParams> {

    private static String KEY_PREFIX = "ding-robot:rl";

    private static String SEPARATOR = ":";

    private RedisTemplate redisTemplate;

    public RedisTaskEnforcer(RedisTemplate redisTemplate) {
        this(KEY_PREFIX, SEPARATOR, redisTemplate);
    }

    public RedisTaskEnforcer(String KEY_PREFIX, String SEPARATOR, RedisTemplate redisTemplate) {
        this.KEY_PREFIX = KEY_PREFIX;
        this.SEPARATOR = SEPARATOR;
        this.redisTemplate = redisTemplate;
    }


    public String getKey(String key) {
        return StringUtils.join(Arrays.asList(KEY_PREFIX, key), SEPARATOR);
    }


    @Override
    public <R> R execute(RedisTaskParams params) {
        //获取redisKey
        String key = getKey(params.getKey());
        //构造脚本, redisTemplate内部实现了sha1脚本调用
        DefaultRedisScript<Long> script = new DefaultRedisScript<>();
        script.setScriptSource(new ResourceScriptSource(new ClassPathResource(params.getScriptPath())));
        script.setResultType(Long.class);
        //注意lua脚本只返回8字节数字类型
        Object execute = redisTemplate.execute(script, Arrays.asList(key), params.getLimit(), params.getWindowSize(), params.getTryAcquireRemain());
        R result = (R) execute;
        return result;
    }
}
