package com.study.rocketmq.version01;

import java.util.ArrayList;
import java.util.List;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;

public class SyncProducer {
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
		// ͬ����Ϣ���ͣ��ȵ�broker��ack֮��Ż����
		defaultMQProducer.send(msg);

		// ������������
		// ������ϢҪ���Ҫ����ͬһtopic����ͬ��Ϣ����
		// ��֧����ʱ��Ϣ
		// ����һ��������Ϣ��ò�Ҫ����1MB��С
		// �����ȷ���Ƿ񳬹����ƣ������ֶ������С��������
		List<Message> list = new ArrayList<>();
		list.add(msg);
		defaultMQProducer.send(list);
		// �ر�����
		defaultMQProducer.shutdown();
	}
}
