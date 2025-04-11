package com.rmq.c.listener;


import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

@Component
@RocketMQMessageListener(topic = "bootTestTopic", consumerGroup = "boot-test-consumer-group")
public class SimpleListener implements RocketMQListener<MessageExt> {
    /**
     * 这里的泛型指定的就是消息体的类型
     * MessageExt会获取消息的所有内容
     * ---------------------------
     * 只要是正常的消费没有不报错 就会自动签收消息
     * 否则拒收消息
     */

    @Override
    public void onMessage(MessageExt message) {
        System.out.println(new String(message.getBody()));
    }
}
