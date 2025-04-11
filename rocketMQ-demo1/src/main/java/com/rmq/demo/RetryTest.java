package com.rmq.demo;

import com.rmq.constant.MqConstant;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

public class RetryTest {

    @Test
    public void retryProducer() throws Exception {

        DefaultMQProducer producer = new DefaultMQProducer("retry-producer-group");
        producer.setNamesrvAddr(MqConstant.NAME_SERVER);
        producer.start();
        //生产者发送消息，重试的次数默认是2
        producer.setRetryTimesWhenSendFailed(2);
        producer.setRetryTimesWhenSendAsyncFailed(2);
        Message message1 = new Message("retryTopic", "我是vip1的文章".getBytes());
        producer.send(message1);
        System.out.println("发送成功");
    }

    /**
     * 默认的时间间隔按照等待的等级来划分，从级别3开始默认重复16次
     * 1.默认 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
     * 2.可系定义重试次数
     * 3.消息处理失败时可自定义处理业务
     * @throws Exception
     */
    //重试次数一般5次以内
    @Test
    public void retryConsumer() throws Exception{
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("retry-consumer-group");
        consumer.setNamesrvAddr(MqConstant.NAME_SERVER);
        consumer.subscribe("retryTopic","*");
        //设定重试次数
        consumer.setMaxReconsumeTimes(2);
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {

                MessageExt messageExt = msgs.get(0);
                System.out.println(new Date());
                System.out.println(messageExt.getReconsumeTimes());
                System.out.println(new String(messageExt.getBody()));
                //业务报错和返回RECONSUME_LATER都会重试
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }
        });
        consumer.start();
        System.in.read();
    }

    /**
     * 达到最大重试次数后，消息进入死信队列
     * 死信队列的主题为 %DLQ% +消费者的组名
     * @throws Exception
     */
    @Test
    public void retryDeadConsumer() throws Exception{
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("retry-dead-consumer-group");
        consumer.setNamesrvAddr(MqConstant.NAME_SERVER);
        consumer.subscribe("%DLQ%retry-consumer-group","*");

        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                MessageExt messageExt = msgs.get(0);
                System.out.println(new Date());
                System.out.println("记录到特别的位置 文件、mysql、或通知用户处理");
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }
        });
        consumer.start();
        System.in.read();
    }

    /**
     * 常用方案
     * 另一种方式，可以避免产生过多的死信消息
     * @throws Exception
     */
    @Test
    public void retryConsumer2() throws Exception{
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("retry-consumer-group");
        consumer.setNamesrvAddr(MqConstant.NAME_SERVER);
        consumer.subscribe("retryTopic","*");
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                MessageExt messageExt = msgs.get(0);
                System.out.println(new Date());
                try {
                    handleDb();
                } catch (Exception e) {
                    int reconsumeTimes = messageExt.getReconsumeTimes();
                    if(reconsumeTimes >= 2){
                        return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                    }
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }

                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        consumer.start();
        System.in.read();
    }
    public static int handleDb(){
        return 10/0;
    }
}
