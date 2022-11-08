package cn.xpp011.dingrobot;

import cn.xpp011.dingrobot.config.DingRobotProperties;
import cn.xpp011.dingrobot.config.DingRobotProperties.RobotProperties;
import cn.xpp011.dingrobot.executor.TaskEnforcer;
import cn.xpp011.dingrobot.ratelimiter.RateLimiter;
import cn.xpp011.dingrobot.ratelimiter.RateLimiterFactory;
import cn.xpp011.dingrobot.ratelimiter.RateLimiterType;
import cn.xpp011.dingrobot.storage.FailMessageQueue;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.BeanCreationException;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * 创建获取DingRobotTemplate
 *
 * @author: xpp011 2022-08-11 13:10
 **/

public class DingRobotFactory {

    private Map<String, DingRobotTemplate> dingRobotMap;

    private ExecutorService executor;

    private FailMessageQueue failMessageQueue;

    public DingRobotFactory(DingRobotProperties properties, ExecutorService executor, FailMessageQueue failMessageQueue, TaskEnforcer taskEnforcer, RateLimiterType rateLimiterType) {
        this.executor = executor;
        this.failMessageQueue = failMessageQueue;
        Map<String, RobotProperties> instance;
        if (properties == null || (instance = properties.getInstance()) == null) return;

        OkHttpClient okHttpClient = createOkHttpClient();
        dingRobotMap = new HashMap<>();
        for (String name : instance.keySet()) {
            RobotProperties robotProperties = instance.get(name);
            try {
                //创建限流器
                RateLimiter rateLimiter = RateLimiterFactory.getRateLimiter(rateLimiterType, taskEnforcer, name);
                dingRobotMap.put(name, creatDingRobot(okHttpClient, robotProperties, properties.getRetry(), rateLimiter, name));
            } catch (Exception e) {
                //创建bean异常
                throw new BeanCreationException(String.format("exception while creating dingRobot: %s", name, e.getMessage()));
            }
        }
    }

    private DingRobotTemplate creatDingRobot(OkHttpClient client, RobotProperties properties, int retry, RateLimiter rateLimiter, String robotName) throws IllegalArgumentException {
        return new DingRobotTemplate(client, properties, retry, executor, rateLimiter, failMessageQueue, robotName);
    }

    private OkHttpClient createOkHttpClient() {
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .build();
        return httpClient;
    }

    public DingRobotTemplate getDingRobot(String dingRobotName) {
        if (!dingRobotMap.containsKey(dingRobotName)) {
            throw new RuntimeException(dingRobotName + " dingRobot is not configured");
        }
        return dingRobotMap.get(dingRobotName);
    }


}
