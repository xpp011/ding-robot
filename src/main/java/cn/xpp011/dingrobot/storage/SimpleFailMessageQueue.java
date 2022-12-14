package cn.xpp011.dingrobot.storage;

import cn.xpp011.dingrobot.message.FailMessage;
import org.springframework.util.Assert;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 本地失败消息队列
 *
 * @author: xpp011 2022-09-05 23:15
 **/

public class SimpleFailMessageQueue implements FailMessageQueue {

    private Queue<FailMessage> queue = new ConcurrentLinkedQueue<>();

    @Override
    public boolean push(FailMessage message) {
        Assert.notNull(message, "failMessage argument is required");
        return queue.offer(message);
    }

    @Override
    public FailMessage pop() {
        return queue.poll();
    }

    @Override
    public long size() {
        return queue.size();
    }

    @Override
    public boolean isEmpty() {
        return queue.isEmpty();
    }
}
