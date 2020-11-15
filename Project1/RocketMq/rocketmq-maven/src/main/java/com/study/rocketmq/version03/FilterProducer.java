package com.study.rocketmq.version03;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

public class FilterProducer {
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
		//��������
		msg.putUserProperty("age", "16");
		// ֻ������Ϣ�����ȴ���������Ӧ��ֻ�������󲻵ȴ�Ӧ�𡣴˷�ʽ������Ϣ�Ĺ��̺�ʱ�ǳ��̣�һ����΢�뼶��
//		defaultMQProducer.sendOneway(msg);
		// ʧ����Ͷ����
		defaultMQProducer.setRetryTimesWhenSendAsyncFailed(2);
		// �첽��Ϣ���ͣ�������send������shutdown����Ϊack���첽���صġ�����ʱ���
		defaultMQProducer.send(msg, new SendCallback() {

			@Override
			public void onSuccess(SendResult sendResult) {
				System.out.println("���ͳɹ�");
			}

			@Override
			public void onException(Throwable e) {
				System.out.println("����ʧ��");
			}
		});
	}
}
