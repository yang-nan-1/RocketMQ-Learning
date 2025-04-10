package com.rmq.demo;

import com.rmq.constant.MqConstant;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.junit.jupiter.api.Test;

import java.util.List;

public class TagTest {

    /**
     * 模拟公众号为不同的用户发送各自订阅的vip文章
     * @throws Exception
     */
    @Test
    public void tagTest() throws Exception {

        DefaultMQProducer producer = new DefaultMQProducer("tag-producer-group");
        producer.setNamesrvAddr(MqConstant.NAME_SERVER);
        producer.start();
        Message message1 = new Message("tegTopic", "vip1", "我是vip1的文章".getBytes());
        Message message2 = new Message("tegTopic", "vip2", "我是vip2的文章".getBytes());
        producer.send(message1);
        producer.send(message2);
        System.out.println("发送成功");
    }

    /**
     * 订阅vip1
     * @throws Exception
     */
    @Test
    public void tagConsumer1() throws Exception{
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("tag-consumer-group-1");
        consumer.setNamesrvAddr(MqConstant.NAME_SERVER);
        consumer.subscribe("tegTopic","vip1");
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                System.out.println("我是vip1的消费者，我正在消费消息："+new String(msgs.get(0).getBody()));
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        consumer.start();
        System.in.read();
    }

    /**
     * 订阅vip1和vip2
     * @throws Exception
     */
    @Test
    public void tagConsumer2() throws Exception{
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("tag-consumer-group-2");
        consumer.setNamesrvAddr(MqConstant.NAME_SERVER);
        consumer.subscribe("tegTopic","vip1 || vip2");

        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                System.out.println("我是vip1和vip2的消费者，我正在消费消息："+new String(msgs.get(0).getBody()));
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        consumer.start();
        System.in.read();
    }
}
