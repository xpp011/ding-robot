package cn.xpp011.dingrobot.message;

import com.google.common.base.MoreObjects;

/**
 * 失败消息
 *
 * @author: xpp011 2022-08-21 21:15
 **/

public class FailMessage {

    private String robotName;

    private Message message;

    /**
     * 重试次数
     */
    private int count;

    public FailMessage(String robotName, Message message) {
        this.robotName = robotName;
        this.message = message;
        this.count = 0;
    }

    public FailMessage() {
    }

    public String getRobotName() {
        return robotName;
    }

    public Message getMessage() {
        return message;
    }

    public int getCount() {
        return count;
    }

    public int increment() {
        return ++count;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("robotName", robotName)
                .add("message", message)
                .add("count", count)
                .toString();
    }
}
