package cn.xpp011.dingrobot.message;

import com.google.common.collect.ImmutableMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: ding-robot
 * @description: FeedCard消息类型
 * @author: xpp011
 * @create: 2022-09-15 23:45
 **/

public class FeedCardMessage extends Message {
    public FeedCardMessage(String msgtypeValue, Map body) {
        super(msgtypeValue, body, new At());
    }

    public static class Builder extends MessageBuilder {

        private List<Map<String, String>> links = new ArrayList<>();

        /**
         * 添加link
         * @param title 单条信息文本
         * @param messageURL 跳转链接(<a href="https://open.dingtalk.com/document/orgapp-server/message-link-description">消息链接打开方式</a>)
         * @param picURL 图片URL
         * @return
         */
        public Builder addLink(String title, String messageURL, String picURL) {
            links.add(ImmutableMap.of(
                    "title", title
                    , "messageURL", messageURL
                    , "picURL", picURL));
            return this;
        }

        @Override
        protected Map<String, Object> builderBody() {
            Map<String, Object> body = new HashMap<>();
            body.put("links", links);
            return body;
        }

        @Override
        protected MessageType messageType() {
            return MessageType.FEED_CARD;
        }
    }


}
