package cn.xpp011.dingrobot.message;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: ding-robot
 * @description:
 * @author: xpp011
 * @create: 2022-08-01 22:15
 **/

public class LinkMessage extends Message {

    private LinkMessage(String msgtypeValue, Map body, At atValue) {
        super(msgtypeValue, body, atValue);
    }

    public static class Builder extends MessageBuilder {

        private String text;

        private String title;

        private String picUrl;

        private String messageUrl;

        public Builder setText(String text) {
            this.text = text;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        /**
         * 图片url
         *
         * @param picUrl
         * @return
         */
        public Builder setPicUrl(String picUrl) {
            this.picUrl = picUrl;
            return this;
        }

        /**
         * 消息转跳连接
         *
         * @param messageUrl
         * @return
         * @see <a href="https://open.dingtalk.com/document/orgapp-server/message-link-description">消息链接打开方式</a>
         */
        public Builder setMessageUrl(String messageUrl) {
            this.messageUrl = messageUrl;
            return this;
        }

        /**
         * link消息不支持@人员
         *
         * @param at
         * @return
         */
        @Deprecated
        public Builder setAt(At at) {
            this.at = at;
            return this;
        }

        @Override
        protected Map<String, Object> builderBody() {
            Map<String, Object> body = new HashMap<>();
            body.put("text", text);
            body.put("title", title);
            body.put("picUrl", picUrl);
            body.put("messageUrl", messageUrl);
            return body;
        }

        @Override
        protected MessageType messageType() {
            return MessageType.LINK;
        }
    }

}
