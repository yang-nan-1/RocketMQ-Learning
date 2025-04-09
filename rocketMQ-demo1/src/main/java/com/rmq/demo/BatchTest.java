package com.rmq.demo;

import com.rmq.constant.MqConstant;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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

    @Test
    public void batchConsumer() throws Exception{
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("batch-consumer-group");
        consumer.setNamesrvAddr(MqConstant.NAME_SERVER);
        consumer.subscribe("batchTopic","*");
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                System.out.println("收到消息了"+new Date());
                System.out.println(new String(msgs.get(0).getBody()));
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        consumer.start();
        System.in.read();
    }
}
