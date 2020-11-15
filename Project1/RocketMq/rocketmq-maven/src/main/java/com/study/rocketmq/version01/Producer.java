package com.study.rocketmq.version01;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;

public class Producer {
	public static void main(String[] args) throws Exception {
		// ����producer��
		DefaultMQProducer defaultMQProducer = new DefaultMQProducer("producerGroup01");
		// ����nameserver�ķ����ַ
		defaultMQProducer.setNamesrvAddr("192.168.73.200:9876");
		// ����
		defaultMQProducer.start();
		String message = "rocketmq�ĵ�һ����Ϣ������ʱ�䣺" + System.currentTimeMillis();
		// topic ��Ϣ��Ҫ���͵��ĵ�ַ
		// body ��Ϣ�еľ�������
		Message msg = new Message("topic01", message.getBytes());
		defaultMQProducer.send(msg);
		//�ر�����
		defaultMQProducer.shutdown();
	}
}
