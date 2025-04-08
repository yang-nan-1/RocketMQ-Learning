package com.rmq.demo;

import com.rmq.constant.MqConstant;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ASimpleTest {
    /**
     * 发消息
     */
    //生产者
    @Test
     public void contextLoads() throws Exception {
        //创建一个生产者
        DefaultMQProducer producer = new DefaultMQProducer("test-producer-group");
        //连接nameserver
        producer.setNamesrvAddr(MqConstant.NAME_SERVER);
        //启动
        producer.start();

        //创建一个消息
        Message message = new Message("testTopic","我是一个简单的消息".getBytes());

        //发送消息
        SendResult sendResult = producer.send(message);
        System.out.println(sendResult.getSendStatus());
        //关闭生产者
        producer.shutdown();
    }

    //消费者
    @Test
    public void consumer() throws Exception {
        //创建一个消费者
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("test-consumer-group");
        //连接nameserver
        consumer.setNamesrvAddr(MqConstant.NAME_SERVER);
        //订阅一个主题  * 标识订阅主题中所有消息
        consumer.subscribe("testTopic","*");
        //设置一个监听器
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                //消费方法（业务处理）
                System.out.println("我是消费者");
                System.out.println(msgs.get(0).toString());
                System.out.println("消息内容:"+new String(msgs.get(0).getBody()));
                System.out.println("消费上下文:"+context);
                //返回CONSUME_SUCCESS 成功 消息从mq出队
                //返回RECONSUME_LATER（报错|null） 重新回到mq队列中排队，等待下一次消费
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        consumer.start();
        System.in.read();
    }
}
