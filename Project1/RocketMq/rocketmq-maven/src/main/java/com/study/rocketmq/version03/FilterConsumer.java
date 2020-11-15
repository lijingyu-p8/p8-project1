package com.study.rocketmq.version03;

import java.util.List;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MessageSelector;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;

public class FilterConsumer {
	public static void main(String[] args) throws Exception {
		// ����������(ע�⣬ͬһ�飬���ܹ�ע��ͬ��topic�Լ�tag�ȵĹ��ˣ�������Ϣ����)
		DefaultMQPushConsumer defaultMQPushConsumer = new DefaultMQPushConsumer("consumerGroup01");
		defaultMQPushConsumer.setNamesrvAddr("192.168.73.200:9876");
		// ����topic����Ϣ
		MessageSelector bySql = MessageSelector.bySql("age > 10");
		defaultMQPushConsumer.subscribe("topic01", bySql);
		defaultMQPushConsumer.registerMessageListener(new MessageListenerConcurrently() {

			@Override
			public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
				for (int i = 0; i < msgs.size(); i++) {
					MessageExt messageExt = msgs.get(i);
					String message = new String(messageExt.getBody());
					System.out.println(message);
				}
				// Ĭ������� ������Ϣֻ�ᱻ һ��consumer ���ѵ� ��Ե�
				// message ״̬�޸�
				// ack
				// �������ѳɹ�
				return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
			}
		});
		defaultMQPushConsumer.start();
		System.out.println("Consumer start...");
	}
}
