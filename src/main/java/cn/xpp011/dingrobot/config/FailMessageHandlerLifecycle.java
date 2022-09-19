package cn.xpp011.dingrobot.config;

import cn.xpp011.dingrobot.FailMessageHandler;
import org.springframework.context.SmartLifecycle;

/**
 * @author: xpp011 2022-09-01 22:56
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
