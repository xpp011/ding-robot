package cn.xpp011.dingrobot.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import cn.xpp011.dingrobot.executor.SimpleTaskEnforcer;
import cn.xpp011.dingrobot.executor.TaskEnforcer;
import cn.xpp011.dingrobot.storage.FailMessageQueue;
import cn.xpp011.dingrobot.storage.SimpleFailMessageQueue;

/**
 * @program: ding-robot
 * @description: 本地钉钉机器人配置
 * @author: xpp011
 * @create: 2022-09-10 22:57
 **/
@Configuration(proxyBeanMethods = false)
public class SimpleDingRobotConfiguration {

    @ConditionalOnMissingBean(FailMessageQueue.class)
    @Bean
    public FailMessageQueue failMessageQueue() {
        return new SimpleFailMessageQueue();
    }

    @ConditionalOnMissingBean(TaskEnforcer.class)
    @Bean
    public TaskEnforcer<?> taskEnforcer() {
        return new SimpleTaskEnforcer();
    }
}
