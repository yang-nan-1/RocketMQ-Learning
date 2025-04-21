package com.rmq.seckill.controller;

import jakarta.annotation.Resource;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SeckillController {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private RocketMQTemplate rocketMQTemplate;
    @GetMapping("/seckill")
    public String doSeckill(@RequestParam("userId") Integer userId,@RequestParam("goodsId")Integer goodsId){
        //uniqueKey
        String uk = userId+"-"+goodsId;
        Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent(uk, "");
        if(!flag){
            System.out.println("您已参与过本活动~~");
        }
        //TODO 库存的预扣减

        Long count = stringRedisTemplate.opsForValue().decrement("goodsId:" + goodsId);
        if(count<0)
            return "库存不足";

        //mq异步处理
        rocketMQTemplate.asyncSend("seckillTopic", uk, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                System.out.println("发送成功");
            }

            @Override
            public void onException(Throwable e) {
                System.out.println("发送失败："+e.getMessage());
            }
        });

        return "正在拼命抢购中，请稍后取查看";
    }
}
