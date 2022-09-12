package cn.xpp011.dingrobot.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import cn.xpp011.dingrobot.executor.RedisTaskEnforcer;
import cn.xpp011.dingrobot.executor.TaskEnforcer;
import cn.xpp011.dingrobot.storage.FailMessageQueue;
import cn.xpp011.dingrobot.storage.RedisFailMessageQueue;

/**
 * @program: ding-robot
 * @description: redis钉钉机器人配置
 * @author: xpp011
 * @create: 2022-09-07 22:49
 **/
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(RedisConnectionFactory.class)
@ConditionalOnBean(RedisConnectionFactory.class)
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class RedisDingRobotConfiguration {

    @ConditionalOnMissingBean(name = "dingRobotRedisTemplate")
    @Bean
    public RedisTemplate dingRobotRedisTemplate(RedisConnectionFactory factory) {
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);

        RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.setHashKeySerializer(jackson2JsonRedisSerializer);
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }

    @ConditionalOnMissingBean(FailMessageQueue.class)
    @Bean
    public FailMessageQueue failMessageQueue(RedisTemplate dingRobotRedisTemplate, DingRobotProperties properties) {
        return new RedisFailMessageQueue(dingRobotRedisTemplate, properties.getRetry());
    }

    @ConditionalOnMissingBean(TaskEnforcer.class)
    @Bean
    public TaskEnforcer<?> taskEnforcer(RedisTemplate dingRobotRedisTemplate) {
        return new RedisTaskEnforcer(dingRobotRedisTemplate);
    }


}
