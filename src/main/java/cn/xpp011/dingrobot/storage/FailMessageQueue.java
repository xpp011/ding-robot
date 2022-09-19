package cn.xpp011.dingrobot.storage;

import cn.xpp011.dingrobot.message.FailMessage;

/**
 * 失败队列
 *
 * @author: xpp011 2022-08-21 21:09
 **/

public interface FailMessageQueue {

    /**
     * 添加至队尾
     *
     * @param message 发送失败消息
     * @return 是否成功
     */
    boolean push(FailMessage message);

    /**
     * 弹出队头的一个元素
     *
     * @return FailMessage
     */
    FailMessage pop();

    /**
     * 队列大小
     *
     * @return size
     */
    long size();

    /**
     * 队列是否为空
     *
     * @return isEmpty
     */
    boolean isEmpty();
}
