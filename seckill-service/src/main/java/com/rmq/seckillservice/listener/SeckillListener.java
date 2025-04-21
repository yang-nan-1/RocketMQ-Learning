package com.rmq.seckillservice.listener;

import com.rmq.seckillservice.service.GoodsService;
import jakarta.annotation.Resource;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

@Component
@RocketMQMessageListener(topic = "seckillTopic",
        consumerGroup = "seckill-consumer-group",
        consumeMode = ConsumeMode.CONCURRENTLY,
        consumeThreadNumber = 32)
public class SeckillListener implements RocketMQListener<MessageExt> {

    @Resource
    private GoodsService goodsService;
    /**
     * 扣减库存
     * 写订单表
     * @param message
     */
    @Override
    public void onMessage(MessageExt message) {
        String msg = new String(message.getBody());
        Integer userId = Integer.parseInt(msg.split("-")[0]);
        Integer goodsId = Integer.parseInt(msg.split("-")[1]);
        goodsService.realSeckill(userId,goodsId);
    }
}
