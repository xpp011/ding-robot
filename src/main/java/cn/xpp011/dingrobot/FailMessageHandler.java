package cn.xpp011.dingrobot;

import cn.xpp011.dingrobot.message.FailMessage;

/**
 * 失败消息处理器
 *
 * @author: xpp011 2022-08-29 23:00
 **/

public interface FailMessageHandler {


    /**
     * 处理消息
     *
     * @param message message
     * @return 处理是否成功
     * @throws Exception 处理消息失败后抛出
     */
    boolean handleMessage(FailMessage message) throws Exception;

    /**
     * 获取失败消息
     *
     * @return FailMessage
     */
    FailMessage getFailMessage();

    /**
     * 启动
     */
    void start();

    /**
     * 停止
     */
    void close();

    /**
     * @return 运行状态
     */
    boolean isRunning();
}
