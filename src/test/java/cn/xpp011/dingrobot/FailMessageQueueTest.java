package cn.xpp011.dingrobot;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;
import cn.xpp011.dingrobot.message.FailMessage;
import cn.xpp011.dingrobot.message.Message;
import cn.xpp011.dingrobot.storage.FailMessageQueue;

/**
 * @program: ding-robot
 * @description:
 * @author: xpp011
 * @create: 2022-09-01 23:22
 **/

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FailMessageQueueTest {

    private static final Logger log = LoggerFactory.getLogger(FailMessageQueueTest.class);

    @Autowired
    private FailMessageQueue queue;

    @Test
    @Order(1)
    public void pushTest() {
        Message message = MessageTest.builderTextMessage();
        FailMessage failMessage = new FailMessage("test", message);
        boolean push = queue.push(failMessage);
        Assert.isTrue(push, "push fail");
    }

    @Test
    @Order(2)
    public void popTest() {
        FailMessage failMessage = queue.pop();
        log.info("{}", failMessage);
        Assert.notNull(failMessage, "pop fail");
    }
}
