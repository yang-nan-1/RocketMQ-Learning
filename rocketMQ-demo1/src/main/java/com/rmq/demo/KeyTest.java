package com.rmq.demo;

import com.rmq.constant.MqConstant;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

public class KeyTest {

    /**
     * key的作用
     * 确保自身唯一，同时在消费时方便查阅和去重
     * @throws Exception
     */
    @Test
    public void keyProducer() throws Exception {

        DefaultMQProducer producer = new DefaultMQProducer("key-producer-group");
        producer.setNamesrvAddr(MqConstant.NAME_SERVER);
        producer.start();
        String key = UUID.randomUUID().toString();
        System.out.println(key);
        Message message1 = new Message("keyTopic", "vip1",key, "我是vip1的文章".getBytes());
        producer.send(message1);
        System.out.println("发送成功");
    }

    @Test
    public void keyConsumer() throws Exception{
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("key-consumer-group-1");
        consumer.setNamesrvAddr(MqConstant.NAME_SERVER);
        consumer.subscribe("keyTopic","vip1");
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                MessageExt messageExt = msgs.get(0);
                System.out.println("我是vip1的消费者，我正在消费消息："+new String(messageExt.getBody()));
                System.out.println("业务标识"+messageExt.getKeys());
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        consumer.start();
        System.in.read();
    }
}

