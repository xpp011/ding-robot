package cn.xpp011.dingrobot.config;

import cn.xpp011.dingrobot.DefaultFailMessageHandler;
import cn.xpp011.dingrobot.DingRobotFactory;
import cn.xpp011.dingrobot.FailMessageHandler;
import cn.xpp011.dingrobot.config.DingRobotAutoConfiguration.DingRobotConfigurationImportSelector;
import cn.xpp011.dingrobot.executor.TaskEnforcer;
import cn.xpp011.dingrobot.ratelimiter.RateLimiterType;
import cn.xpp011.dingrobot.storage.FailMessageQueue;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * 钉钉机器人自动配置类
 *
 * @author: xpp011 2022-08-01 10:21
 **/
@Configuration
@ConditionalOnMissingBean(DingRobotFactory.class)
@EnableConfigurationProperties(DingRobotProperties.class)
@AutoConfigureAfter({RedisAutoConfiguration.class})
@Import({DingRobotConfigurationImportSelector.class})
public class DingRobotAutoConfiguration {

    private final DingRobotProperties properties;

    public DingRobotAutoConfiguration(DingRobotProperties properties) {
        this.properties = properties;
    }

    @ConditionalOnMissingBean(name = "dingRobotExecutor")
    @Bean
    public ExecutorService dingRobotExecutor() {
        int corePoolSize = properties.getCorePoolSize();
        ExecutorService executor = createExecutor(corePoolSize);
        return executor;
    }

    @ConditionalOnMissingBean(DingRobotFactory.class)
    @ConditionalOnBean(value = {TaskEnforcer.class, FailMessageQueue.class}, name = {"dingRobotExecutor"})
    @Bean
    public DingRobotFactory dingRobotFactory(ExecutorService dingRobotExecutor, FailMessageQueue failMessageQueue, TaskEnforcer<?> taskEnforcer) {
        return new DingRobotFactory(properties, dingRobotExecutor, failMessageQueue, taskEnforcer, RateLimiterType.SLIDING_WINDOW);
    }

    @ConditionalOnMissingBean(FailMessageHandler.class)
    @Bean
    public FailMessageHandler failMessageHandler(FailMessageQueue failMessageQueue, DingRobotFactory dingRobotFactory) {
        return new DefaultFailMessageHandler(failMessageQueue, dingRobotFactory, properties.getRetry());
    }

    @Bean
    public FailMessageHandlerLifecycle failMessageHandlerLifecycle(FailMessageHandler failMessageHandler) {
        return new FailMessageHandlerLifecycle(failMessageHandler);
    }

    private ExecutorService createExecutor(int corePoolSize) {
        ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat("DingRobot-Thread-%d")
                .build();
        ExecutorService executorService = Executors.newFixedThreadPool(corePoolSize, threadFactory);
        return executorService;
    }

    static class DingRobotConfigurationImportSelector implements ImportSelector {
        @Override
        public String[] selectImports(AnnotationMetadata importingClassMetadata) {
            DingRobotType[] types = DingRobotType.values();
            int n = types.length;
            String[] imports = new String[n];
            for (int i = 0; i < n; i++) {
                imports[i] = types[i].getClassName();
            }
            return imports;
        }
    }
}
