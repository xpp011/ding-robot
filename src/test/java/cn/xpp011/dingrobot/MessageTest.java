package cn.xpp011.dingrobot;

import cn.xpp011.dingrobot.message.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

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
                .setAtMobiles(Arrays.asList("xxxxxxxxxxx"))
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
        Message link = new LinkMessage.Builder()
                .setTitle("标题")
                .setText("link消息")
                .setMessageUrl("https://open.dingtalk.com/document/group/custom-robot-access")
                .setPicUrl("https://help-static-aliyun-doc.aliyuncs.com/assets/img/zh-CN/4099076061/p131227.png")
                .builder();
        print(link);
        Assert.notEmpty(link, "构造link消息失败");
        return link;
    }

    @Test
    public static Message builderActionCardMessage() {
        Message message = new ActionCardMessage.Builder()
                .setTitle("我 20 年前想打造一间苹果咖啡厅，而它正是 Apple Store 的前身")
                .setText("![screenshot](https://gw.alicdn.com/tfs/TB1ut3xxbsrBKNjSZFpXXcXhFXa-846-786.png) \n" +
                        " ### 乔布斯 20 年前想打造的苹果咖啡厅 \n" +
                        " Apple Store 的设计正从原来满满的科技感走向生活化，而其生活化的走向其实可以追溯到 20 年前苹果一个建立咖啡馆的计划")
                .setBtnOrientation(false)
                .setSingleTitle("阅读全文")
                .setSingleURL("https://www.dingtalk.com/")
                .builder();
        print(message);
        Assert.notEmpty(message, "构造ActionCard消息失败");
        return message;
    }

    @Test
    public static Message builderActionCardMessageBtns() {
        Message message = new ActionCardMessage.Builder()
                .setTitle("我 20 年前想打造一间苹果咖啡厅，而它正是 Apple Store 的前身")
                .setText("![screenshot](https://gw.alicdn.com/tfs/TB1ut3xxbsrBKNjSZFpXXcXhFXa-846-786.png) \n" +
                        " ### 乔布斯 20 年前想打造的苹果咖啡厅 \n" +
                        " Apple Store 的设计正从原来满满的科技感走向生活化，而其生活化的走向其实可以追溯到 20 年前苹果一个建立咖啡馆的计划")
                .setBtnOrientation(true)
                .addBtn("浏览器打开", "dingtalk://dingtalkclient/page/link?url=https%3A%2F%2Fwww.dingtalk.com%2F&pc_slide=false")
                .addBtn("PC端侧边栏打开", "dingtalk://dingtalkclient/page/link?url=https%3A%2F%2Fwww.dingtalk.com%2F&pc_slide=true")
                .builder();
        print(message);
        Assert.notEmpty(message, "构造ActionCard消息失败");
        return message;
    }

    @Test
    public static Message builderFeedCardMessage() {
        Message message = new FeedCardMessage.Builder()
                .addLink("乔布斯 20 年前想打造的苹果咖啡厅",
                        "dingtalk://dingtalkclient/page/link?url=https%3A%2F%2Fwww.dingtalk.com%2F&pc_slide=false",
                        "https://gw.alicdn.com/tfs/TB1ut3xxbsrBKNjSZFpXXcXhFXa-846-786.png")
                .addLink("时代的火车向前开1",
                        "dingtalk://dingtalkclient/page/link?url=https%3A%2F%2Fwww.dingtalk.com%2F&pc_slide=true/",
                        "https://img.alicdn.com/tfs/TB1NwmBEL9TBuNjy1zbXXXpepXa-2400-1218.png")
                .addLink("时代的火车向前开2",
                        "https://www.dingtalk.com/",
                        "https://img.alicdn.com/tfs/TB1NwmBEL9TBuNjy1zbXXXpepXa-2400-1218.png")
                .builder();
        print(message);
        Assert.notEmpty(message, "构造FeedCard消息失败");
        return message;
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
