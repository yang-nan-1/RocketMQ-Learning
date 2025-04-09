package com.rmq.demo;

import com.rmq.constant.MqConstant;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BatchTest {

    @Test
    public void testBatchProducer() throws Exception{
        DefaultMQProducer producer = new DefaultMQProducer("batch-producer-group");
        producer.setNamesrvAddr(MqConstant.NAME_SERVER);
        producer.start();
        List<Message> messages = Arrays.asList(
                new Message("batchTopic","我是消息1".getBytes()),
                new Message("batchTopic","我是消息2".getBytes()),
                new Message("batchTopic","我是消息3".getBytes())
        );
        SendResult send = producer.send(messages);
        System.out.println(send);
        producer.shutdown();
    }
}
