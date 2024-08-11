package com.minyu.knowledge.sea.kafka;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Map;

public class KafkaInterceptorMock implements ProducerInterceptor<String, String> {

    /**
     * 数据发送前，会执行此方法，进行数据发送前的预处理
     *
     * @param producerRecord
     * @return
     */
    @Override
    public ProducerRecord<String, String> onSend(ProducerRecord<String, String> producerRecord) {
        return null;
    }

    /**
     * 数据发送后，获取应答时，会执行此方法
     *
     * @param recordMetadata
     * @param e
     */
    @Override
    public void onAcknowledgement(RecordMetadata recordMetadata, Exception e) {

    }

    /**
     * 生产者关闭时，会执行此方法，完成一些资源回收和释放的操作
     */
    @Override
    public void close() {

    }

    /**
     * 创建生产者对象的时候，会执行此方法，可以根据场景对生产者对象的配置进行统一修改或转换。
     *
     * @param map
     */
    @Override
    public void configure(Map<String, ?> map) {

    }
}
