package cn.xpp011.dingrobot.message;

import java.util.Map;

/**
 * @program: ding-robot
 * @description: 建造者模式，所有消息建造器继承该类
 * @author: xpp011
 * @create: 2022-08-01 22:12
 **/

public abstract class MessageBuilder {

    /**
     * at信息，子类需写setAt方法
     */
    protected At at = new At();

    protected abstract Map<String, Object> builderBody();

    protected abstract MessageType messageType();

    public Message builder() {
        Message message = new Message(messageType().getType(), builderBody(), at);
        return message;
    }
}
