package com.rmq.c.listener;

import com.alibaba.fastjson2.JSON;
import com.rmq.c.entity.MsgModel;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

@Component
@RocketMQMessageListener(topic = "bootOrderlyTopic",
        consumerGroup = "boot-orderly-consumer-group",
        consumeMode = ConsumeMode.ORDERLY,   //顺序消费模式
        maxReconsumeTimes = 5 // 最大重试次数
)
public class OrderlyMsgListener implements RocketMQListener<MessageExt> {
    @Override
    public void onMessage(MessageExt message) {
        MsgModel msgModel = JSON.parseObject(new String(message.getBody()), MsgModel.class);
        System.out.println(msgModel.toString());
    }
}
