package com.rmq;

import com.rmq.constant.MqConstant;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;



@SpringBootTest
class RocketMqDemo1ApplicationTests {

	/**
	 * 发消息
	 */
	@Test
	void contextLoads() throws MQClientException, MQBrokerException, RemotingException, InterruptedException {
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

}
