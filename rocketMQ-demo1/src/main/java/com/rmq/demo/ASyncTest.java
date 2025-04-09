package com.rmq.demo;


import com.rmq.constant.MqConstant;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.junit.jupiter.api.Test;

public class ASyncTest {
    @Test
    public void asyncProducer () throws Exception{
        DefaultMQProducer producer = new DefaultMQProducer("async-producer-group");
        producer.setNamesrvAddr(MqConstant.NAME_SERVER);
        producer.start();

        Message message = new Message("asyncTopic","我是一个异步的消息".getBytes());
        producer.send(message, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                System.out.println("发送成功");
            }

            @Override
            public void onException(Throwable e) {
                System.out.println("发送失败"+e.getMessage());
            }
        });

        System.out.println("main线程先执行");
        System.in.read();
        producer.shutdown();
    }
}
