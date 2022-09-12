package cn.xpp011.dingrobot.storage;

import cn.xpp011.dingrobot.message.FailMessage;

/**
 * @program: ding-robot
 * @description: 失败队列
 * @author: xpp011
 * @create: 2022-08-21 21:09
 **/

public interface FailMessageQueue {

    /**
     * 添加至队尾
     *
     * @param message
     * @return
     */
    boolean push(FailMessage message);

    /**
     * 弹出队头的一个元素
     *
     * @return
     */
    FailMessage pop();

    /**
     * 队列大小
     * @return
     */
    long size();

    /**
     * 队列是否为空
     * @return
     */
    boolean isEmpty();
}
