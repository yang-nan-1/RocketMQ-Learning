package com.rmq.rocketmqbootproducer;

import com.alibaba.fastjson.JSON;
import com.rmq.p.RocketMqBootProducerApplication;
import com.rmq.p.entity.MsgModel;
import jakarta.annotation.Resource;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.util.Arrays;
import java.util.List;

@SpringBootTest(classes = RocketMqBootProducerApplication.class)
class RocketMqBootProducerApplicationTests {

    @Resource
    private RocketMQTemplate rocketMQTemplate;


    @Test
    void contextLoads() {

        //同步消息
        rocketMQTemplate.syncSend("bootTestTopic","我是boot的一个同步消息");

        //异步消息
        rocketMQTemplate.asyncSend("bootTestTopic", "我是boot的一个异步消息", new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                System.out.println("成功");
            }

            @Override
            public void onException(Throwable e) {
                System.out.println("失败："+e.getMessage());
            }
        });

        //单项消息
        rocketMQTemplate.sendOneWay("bootOneWayTopic", "我是boot的一个单向消息");

        //延时消息
        Message<String> msg = MessageBuilder.withPayload("我是boot的一个延时消息").build();
        rocketMQTemplate.syncSend("bootMsTopic",msg,3000,3);

        //顺序消息
        List<MsgModel> msgModels = Arrays.asList(
                new MsgModel("orderSn1",1,"下单"),
                new MsgModel("orderSn1",2,"短信"),
                new MsgModel("orderSn1",3,"物流"),

                new MsgModel("orderSn2",1,"下单"),
                new MsgModel("orderSn2",2,"短信"),
                new MsgModel("orderSn2",3,"物流")
        );
        msgModels.forEach(msgModel -> {
            //发送 一般以JSON的方式进行发送
            String jsonString = JSON.toJSONString(msgModel);
            System.out.println(jsonString);
            rocketMQTemplate.syncSendOrderly("bootOrderlyTopic",jsonString,msgModel.getOrderSn());
        });

    }

    @Test
    void tagKeyTest() throws Exception{
        rocketMQTemplate.syncSend(" :tagA","我是一个带tag的消息");

        Message<String> m = MessageBuilder.withPayload("我是一个带key的消息").setHeader(RocketMQHeaders.KEYS, "key1").build();
        rocketMQTemplate.syncSend("bootTestTopic",m);

    }


    //测试消费模式 集群模块 广播模式

    @Test
    void modeTest() throws Exception{
        for (int i = 0; i < 10; i++) {
            rocketMQTemplate.syncSend("modeTopic", "我是第"+i+"个集群模式的消息");
        }
    }

    @Test
    void jyTest() throws Exception{
        for (int i = 0; i < 100000; i++) {
            Thread.sleep(500);
            rocketMQTemplate.syncSend("jyTopic", "我是第"+i+"个消息");
        }
    }

}
