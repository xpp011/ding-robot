package cn.xpp011.dingrobot.message;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: ding-robot
 * @description: text消息类型
 * @author: xpp011
 * @create: 2022-08-01 22:14
 **/

public class TextMessage extends Message {

    private TextMessage(String msgtypeValue, Map body, At atValue) {
        super(msgtypeValue, body, atValue);
    }

    public static class Builder extends MessageBuilder {

        private String content;

        public Builder setContent(String content) {
            this.content = content;
            return this;
        }

        public Builder setAt(At at) {
            this.at = at;
            return this;
        }

        @Override
        protected Map<String, Object> builderBody() {
            Map<String, Object> body = new HashMap<>();
            body.put("content", content);
            return body;
        }

        @Override
        protected MessageType messageType() {
            return MessageType.TEXT;
        }
    }
}
