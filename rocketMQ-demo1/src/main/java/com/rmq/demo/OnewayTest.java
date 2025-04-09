package com.rmq.demo;

import com.rmq.constant.MqConstant;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.junit.jupiter.api.Test;

public class OnewayTest {

    //单项消息主要应用在记录日志和事件通知等应用场景
    @Test
    public void onewayProducer() throws Exception{
        DefaultMQProducer producer = new DefaultMQProducer("oneway-producer-group");
        producer.setNamesrvAddr(MqConstant.NAME_SERVER);
        producer.start();
        Message message = new Message("onewayTopic","日志xxx".getBytes());
        producer.sendOneway(message);
        System.out.println("消息发送完毕");
        producer.shutdown();
    }
}
