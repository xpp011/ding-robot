package cn.xpp011.dingrobot.message;

import com.google.common.collect.ImmutableMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: ding-robot
 * @description: actionCard消息类型
 * @author: xpp011
 * @create: 2022-09-15 22:45
 **/

public class ActionCardMessage extends Message {

    private ActionCardMessage(String msgtypeValue, Map body) {
        super(msgtypeValue, body, new At());
    }

    public static class Builder extends MessageBuilder {

        private String title;

        private String text;

        /**
         * 按钮方向
         */
        private String btnOrientation;

        /**
         * 按钮标题
         */
        private String singleTitle;

        /**
         * 按钮转跳链接
         */
        private String singleURL;

        /**
         * 按钮
         */
        private List<Map<String, String>> btns = new ArrayList<>();

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setText(String text) {
            this.text = text;
            return this;
        }

        public Builder setSingleTitle(String singleTitle) {
            this.singleTitle = singleTitle;
            return this;
        }

        public Builder setSingleURL(String singleURL) {
            this.singleURL = singleURL;
            return this;
        }

        /**
         * 按钮方法
         * button数量>2时横向排列无效
         * @param isRow
         * @return
         */
        public Builder setBtnOrientation(boolean isRow) {
            this.btnOrientation = isRow ? "1" : "0";
            return this;
        }

        /**
         * 添加按钮
         *
         * @param title     按钮标题
         * @param actionURL 转跳链接(<a href="https://open.dingtalk.com/document/orgapp-server/message-link-description">消息链接打开方式</a>)
         * @return
         */
        public Builder addBtn(String title, String actionURL) {
            btns.add(ImmutableMap.of("title", title, "actionURL", actionURL));
            return this;
        }

        @Override
        protected Map<String, Object> builderBody() {
            Map<String, Object> body = new HashMap<>();
            body.put("title", title);
            body.put("text", text);
            body.put("btnOrientation", btnOrientation);
            body.put("singleTitle", singleTitle);
            body.put("singleURL", singleURL);
            body.put("btns", btns);
            return body;
        }

        @Override
        protected MessageType messageType() {
            return MessageType.ACTION_CARD;
        }
    }
}
