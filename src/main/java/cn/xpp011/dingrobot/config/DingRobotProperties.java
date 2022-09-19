package cn.xpp011.dingrobot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * 钉钉机器人配置
 *
 * @author: xpp011 2022-07-18 11:07
 **/
@ConfigurationProperties(prefix = "warn.ding-robot")
public class DingRobotProperties {

    /**
     * 机器人实例
     */
    private Map<String, RobotProperties> instance = new HashMap<>();

    /**
     * 重试次数
     */
    private Integer retry = 2;

    /**
     * 线程池核心线程数量
     */
    private Integer corePoolSize = 3;

    public static class RobotProperties {

        /**
         * 机器人请求地址
         */
        private String webhook;

        /**
         * 机器人密钥
         */
        private String secret;

        public String getWebhook() {
            return webhook;
        }

        public void setWebhook(String webhook) {
            this.webhook = webhook;
        }

        public String getSecret() {
            return secret;
        }

        public void setSecret(String secret) {
            this.secret = secret;
        }

    }

    public Map<String, RobotProperties> getInstance() {
        return instance;
    }

    public void setInstance(Map<String, RobotProperties> instance) {
        this.instance = instance;
    }

    public Integer getRetry() {
        return retry;
    }

    public void setRetry(Integer retry) {
        this.retry = retry;
    }

    public Integer getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(Integer corePoolSize) {
        this.corePoolSize = corePoolSize;
    }
}
