package com.rmq.c.listener;


import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

@Component
@RocketMQMessageListener(topic = "modeTopic",
        consumerGroup = "mode-consumer-group-a",
        messageModel = MessageModel.CLUSTERING//集群模式
)
public class C1 implements RocketMQListener<String> {
    @Override
    public void onMessage(String message) {
        System.out.println("我是mode-consumer-group-a的第一个消费者"+message);
    }
}
