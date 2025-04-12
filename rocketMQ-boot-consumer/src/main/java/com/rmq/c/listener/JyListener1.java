package com.rmq.c.listener;

import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

//解决消息堆积问题
@Component
@RocketMQMessageListener(topic = "jyTopic",
        consumerGroup = "jy-consumer-group",
        consumeThreadMax = 32,
        consumeMode = ConsumeMode.CONCURRENTLY
)
public class JyListener1 implements RocketMQListener<String> {
    @Override
    public void onMessage(String message) {
        System.out.println("我是第一个消费者"+message);
    }
}
