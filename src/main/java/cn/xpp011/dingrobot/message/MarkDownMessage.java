package cn.xpp011.dingrobot.message;

import java.util.HashMap;
import java.util.Map;

/**
 * MarkDown消息类型
 *
 * @author: xpp011 2022-08-01 22:17
 **/

public class MarkDownMessage extends Message {

    private MarkDownMessage(String msgtypeValue, Map body, At atValue) {
        super(msgtypeValue, body, atValue);
    }

    public static class Builder extends MessageBuilder {

        private String title;

        private String text;

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setText(String text) {
            this.text = text;
            return this;
        }

        /**
         * markdown消息格式@人员时请在text书写@[手机号|userid]才会生效
         *
         * @param at at
         * @return Builder
         */
        public Builder setAt(At at) {
            this.at = at;
            return this;
        }

        @Override
        protected Map<String, Object> builderBody() {
            Map<String, Object> body = new HashMap<>();
            body.put("title", title);
            body.put("text", text);
            return body;
        }

        @Override
        protected MessageType messageType() {
            return MessageType.MARKDOWN;
        }
    }

}
