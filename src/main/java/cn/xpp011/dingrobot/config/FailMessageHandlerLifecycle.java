package cn.xpp011.dingrobot.config;

import org.springframework.context.SmartLifecycle;
import cn.xpp011.dingrobot.FailMessageHandler;

/**
 * @program: ding-robot
 * @description:
 * @author: xpp011
 * @create: 2022-09-01 22:56
 **/

public class FailMessageHandlerLifecycle implements SmartLifecycle {

    private FailMessageHandler failMessageHandler;

    public FailMessageHandlerLifecycle(FailMessageHandler failMessageHandler) {
        this.failMessageHandler = failMessageHandler;
    }

    @Override
    public void start() {
        failMessageHandler.start();
    }

    @Override
    public void stop() {
        failMessageHandler.close();
    }

    @Override
    public boolean isRunning() {
        return failMessageHandler.isRunning();
    }
}
