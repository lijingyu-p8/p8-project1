package com.study.rocketmq.version04;

import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

public class TransactionProducer {
	public static void main(String[] args) throws Exception {
		// ����producer��
		TransactionMQProducer defaultMQProducer = new TransactionMQProducer("producerGroup01");
		// ����nameserver�ķ����ַ
		defaultMQProducer.setNamesrvAddr("192.168.73.200:9876");
		// �������������broker��ص�
		defaultMQProducer.setTransactionListener(new TransactionListener() {

			@Override
			public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
				// δ֪״̬
//				LocalTransactionState.UNKNOW;
				// �ع�
//				LocalTransactionState.ROLLBACK_MESSAGE;
				// �ύ
				return LocalTransactionState.COMMIT_MESSAGE;
			}

			@Override
			public LocalTransactionState checkLocalTransaction(MessageExt msg) {
				return null;
			}
		});
		// ����
		defaultMQProducer.start();
		String message = "rocketmq�ĵ�һ����Ϣ������ʱ�䣺" + System.currentTimeMillis();
		// topic ��Ϣ��Ҫ���͵��ĵ�ַ
		// body ��Ϣ�еľ�������
		Message msg = new Message("topic01", message.getBytes());
		// ��������
		msg.putUserProperty("age", "16");
	}
}
