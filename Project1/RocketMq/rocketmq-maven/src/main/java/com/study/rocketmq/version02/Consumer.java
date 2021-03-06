package com.study.rocketmq.version02;

import java.util.List;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

public class Consumer {
	public static void main(String[] args) throws Exception {
		// 设置消费组(注意，同一组，不能关注不同的topic以及tag等的过滤，否则消息会混乱)
		DefaultMQPushConsumer defaultMQPushConsumer = new DefaultMQPushConsumer("consumerGroup01");
		defaultMQPushConsumer.setNamesrvAddr("192.168.73.200:9876");
		// 设置消息过滤器，*为不过啦
		String messageSelector = "*";
		// 订阅topic的消息
		defaultMQPushConsumer.subscribe("topic01", messageSelector);
		defaultMQPushConsumer.registerMessageListener(new MessageListenerConcurrently() {

			@Override
			public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
				for (int i = 0; i < msgs.size(); i++) {
					MessageExt messageExt = msgs.get(i);
					String message = new String(messageExt.getBody());
					System.out.println(message);
				}
				// 默认情况下 这条消息只会被 一个consumer 消费到 点对点
				// message 状态修改
				// ack
				// 返回消费成功
				return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
			}
		});
		// 默认集群模式，可以由此设置为广播模式
		// 消费进度由consumer维护
		// 保证每个消费者消费一次消息
		// 消费失败的消息不会重投
//		defaultMQPushConsumer.setMessageModel(MessageModel.BROADCASTING);
		defaultMQPushConsumer.start();
		System.out.println("Consumer start...");
	}
}
