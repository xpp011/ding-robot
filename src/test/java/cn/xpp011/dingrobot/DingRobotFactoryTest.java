package cn.xpp011.dingrobot;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;
import cn.xpp011.dingrobot.message.*;

/**
 * @program: ding-robot
 * @description: 机器人测试
 * @author: xpp011
 * @create: 2022-08-22 23:40
 **/
@SpringBootTest(classes = DingRobotApplication.class)
public class DingRobotFactoryTest {

    private static final Logger log = LoggerFactory.getLogger(DingRobotFactoryTest.class);

    @Autowired
    private DingRobotFactory dingRobotFactory;



    @Test
    public void testSendMarkDownMessage() {
        DingRobotTemplate dingRobot = dingRobotFactory.getDingRobot("message-fail");
        Message message = MessageTest.builderMarkDownMessage();
        boolean success = dingRobot.send(message);
        Assert.isTrue(success, "发送失败");
    }



    @Test
    public void testSendTextMessage() {
        DingRobotTemplate dingRobot = dingRobotFactory.getDingRobot("message-fail");
        Message text = MessageTest.builderTextMessage();
        boolean success = dingRobot.send(text);
        Assert.isTrue(success, "发送失败");
    }




    @Test
    public void testSendLinkMessage() {
        DingRobotTemplate dingRobot = dingRobotFactory.getDingRobot("message-fail");
        Message link = MessageTest.builderLinkMessage();
        boolean success = dingRobot.send(link);
        Assert.isTrue(success, "发送失败");
    }


    @Test
    public void testAsyncSend() {
        DingRobotTemplate dingRobot = dingRobotFactory.getDingRobot("message-fail");
        Message message = MessageTest.builderTextMessage();
        dingRobot.sendAsync(message);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
