package cn.xpp011.dingrobot.message;

import java.util.HashMap;
import java.util.Map;

/**
 * link消息类型
 *
 * @author: xpp011 2022-08-01 22:15
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
         * @param picUrl 图片url
         * @return Builder
         */
        public Builder setPicUrl(String picUrl) {
            this.picUrl = picUrl;
            return this;
        }

        /**
         * 消息转跳连接
         *
         * @param messageUrl 消息转跳连接
         * @return Builder
         * @see <a href="https://open.dingtalk.com/document/orgapp-server/message-link-description">消息链接打开方式</a>
         */
        public Builder setMessageUrl(String messageUrl) {
            this.messageUrl = messageUrl;
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
