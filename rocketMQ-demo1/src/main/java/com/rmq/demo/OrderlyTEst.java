package com.rmq.demo;

import com.rmq.constant.MqConstant;
import com.rmq.entity.MsgModel;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.*;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class OrderlyTEst {

    private List<MsgModel> msgModels = Arrays.asList(
            new MsgModel("orderSn1",1,"下单"),
            new MsgModel("orderSn1",2,"短信"),
            new MsgModel("orderSn1",3,"物流"),

            new MsgModel("orderSn2",1,"下单"),
            new MsgModel("orderSn2",2,"短信"),
            new MsgModel("orderSn2",3,"物流")
    );

    @Test
    public void orderly() throws Exception{
        DefaultMQProducer producer = new DefaultMQProducer("orderly-producer-group");
        producer.setNamesrvAddr(MqConstant.NAME_SERVER);
        producer.start();
        //发送顺序消息，发送时要确保有序并且发到同一个队列
        msgModels.forEach(msgModel -> {
            Message message = new Message("orderlyTopic",msgModel.toString().getBytes());
            try {
                //选相同的订单号去相同的队列
                producer.send(message, new MessageQueueSelector() {
                    @Override
                    public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                        //返回一个队列
                        int hashCode = arg.toString().hashCode();
                        int i = hashCode % mqs.size();
                        return mqs.get(i);
                    }
                },msgModel.getOrderSn());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        producer.shutdown();
    }


    @Test
    public void consumer() throws Exception {
        //创建一个消费者
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("orderly-consumer-group");
        //连接nameserver
        consumer.setNamesrvAddr(MqConstant.NAME_SERVER);
        //订阅一个主题  * 标识订阅主题中所有消息
        consumer.subscribe("orderlyTopic","*");
        //MessageListenerConcurrently 并发模式 多线程的
        //MessageListenerOrderly 顺序模式 单线程的
        consumer.registerMessageListener(new MessageListenerOrderly() {
            @Override
            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
                System.out.println("线程id:"+Thread.currentThread().getId());
                System.out.println("消息内容:"+new String(msgs.get(0).getBody()));
                return ConsumeOrderlyStatus.SUCCESS;
            }
        });
        consumer.start();
        System.in.read();
    }

}
