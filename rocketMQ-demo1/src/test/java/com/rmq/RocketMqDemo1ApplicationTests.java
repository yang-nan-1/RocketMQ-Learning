package com.rmq;

import com.rmq.constant.MqConstant;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.UUID;


@SpringBootTest
class RocketMqDemo1ApplicationTests {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 发重复消息
	 */
	@Test
	void repeatProducer() throws Exception {

		DefaultMQProducer producer = new DefaultMQProducer("repeat-producer-group");
		producer.setNamesrvAddr(MqConstant.NAME_SERVER);
		producer.start();
		String key = UUID.randomUUID().toString();
		System.out.println(key);

		Message m1 = new Message("repeatTopic",null,key,"扣减库存-1".getBytes());
		Message m1Repeat = new Message("repeatTopic",null,key,"扣减库存-1".getBytes());

		producer.send(m1);
		producer.send(m1Repeat);
		System.out.println("发送成功");
		producer.shutdown();
	}


	/**
	 *设计去重表 对消息添加唯一key
	 * 每次消费消息时 先插入数据库 如果成功执行业务逻辑
	 * 插入失败证明消息来过了，直接签收
	 * @throws Exception
	 */
	@Test
	void repeteConsumer() throws Exception {
		DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("repete-consumer-group");
		consumer.setNamesrvAddr(MqConstant.NAME_SERVER);
		consumer.subscribe("repeatTopic","*");
		consumer.registerMessageListener(new MessageListenerConcurrently() {
			@Override
			public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
				//先拿key
				MessageExt messageExt = msgs.get(0);
				String key = messageExt.getKeys();
				//插入数据库，有主键做唯一索引
				int i = jdbcTemplate.update("insert into order_oper_log(`type`,`order_sn`,`user`) values (1,?,'123')", key);
				System.out.println(new String(messageExt.getBody()));
				return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
			}
		});
		consumer.start();
		System.in.read();
	}

}
