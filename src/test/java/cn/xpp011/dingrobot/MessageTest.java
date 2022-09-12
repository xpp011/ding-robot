package cn.xpp011.dingrobot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import cn.xpp011.dingrobot.message.*;

import java.util.Arrays;

/**
 * @program: ding-robot
 * @description:
 * @author: xpp011
 * @create: 2022-08-21 19:57
 **/

public class MessageTest {

    private static final Logger log = LoggerFactory.getLogger(MessageTest.class);

    @Test
    public static Message builderMarkDownMessage() {
        At at = new At.Builder()
//                .setAtMobiles(Arrays.asList("xxxxxxxxxxx"))
                .setAtAll(true)
                .builder();

        Message markDown = new MarkDownMessage.Builder()
                .setTitle("正常消息测试")
                .setText(new StringBuilder()
                        .append("> **MarkDown消息@xxxxxxxxxxx**")
                        .toString())
                .setAt(at)
                .builder();
        print(markDown);
        Assert.notEmpty(markDown, "构造markDown消息失败");
        return markDown;
    }

    @Test
    public static Message builderTextMessage() {
        At at = new At.Builder()
                .setAtMobiles(Arrays.asList("xxxxxxxxxxx"))
                .setAtAll(false)
                .builder();
        Message text = new TextMessage.Builder()
                .setContent("Text消息")
                .setAt(at)
                .builder();
        print(text);
        Assert.notEmpty(text, "构造text消息失败");
        return text;
    }

    public static Message builderTextMessage(int i) {
        At at = new At.Builder()
                .setAtMobiles(Arrays.asList("xxxxxxxxxxx"))
                .setAtAll(false)
                .builder();
        Message text = new TextMessage.Builder()
                .setContent("异步调用发送消息:" + i)
                .setAt(at)
                .builder();
        print(text);
        Assert.notEmpty(text, "构造text消息失败");
        return text;
    }

    @Test
    public static Message builderLinkMessage() {
        At at = new At.Builder()
                .setAtMobiles(Arrays.asList("xxxxxxxxxxx"))
                .setAtAll(true)
                .builder();
        Message link = new LinkMessage.Builder()
                .setTitle("标题")
                .setText("link消息")
                .setMessageUrl("https://open.dingtalk.com/document/group/custom-robot-access")
                .setPicUrl("https://help-static-aliyun-doc.aliyuncs.com/assets/img/zh-CN/4099076061/p131227.png")
                .setAt(at)
                .builder();
        print(link);
        Assert.notEmpty(link, "构造link消息失败");
        return link;
    }

    private static void print(Message message) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String s = null;
            s = objectMapper.writeValueAsString(message);
            log.info(s);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
