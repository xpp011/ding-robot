package cn.xpp011.dingrobot.message;

import java.util.HashMap;
import java.util.Map;

/**
 * 钉钉机器人消息体
 *
 * @author: xpp011 2022-07-31 22:47
 **/

public class Message extends HashMap<String, Object> {
    protected Message(String msgtypeValue, Map body, At atValue) {
        super.put("msgtype", msgtypeValue);
        super.put(msgtypeValue, body);
        super.put("at", atValue);
    }

    public Message() {
        super();
    }
}
