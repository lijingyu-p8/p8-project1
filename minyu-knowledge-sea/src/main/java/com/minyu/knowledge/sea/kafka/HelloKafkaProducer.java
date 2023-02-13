package com.minyu.knowledge.sea.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.Future;

/**
 * @Description: kafka生产者
 * @Author: lijingyu
 * @CreateTime: 2023-02-12  23:22
 */
public class HelloKafkaProducer {
    public static void main(String[] args) {
        // 设置属性
        Properties properties = new Properties();
        // 指定连接的kafka服务器的地址
        properties.put("bootstrap.servers", "192.168.152.131:9092");
        // 设置String的序列化
        properties.put("key.serializer", StringSerializer.class);
        properties.put("value.serializer", StringSerializer.class);

        // 构建kafka生产者对象
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);
        try {
            ProducerRecord<String, String> record;
            try {
                // 构建消息
                record = new ProducerRecord<String, String>("msb", "teacher", "lijin");
                // 发送消息
                Future<RecordMetadata> send = producer.send(record);
                RecordMetadata recordMetadata = send.get();
                int partition = recordMetadata.partition();
                String topic = recordMetadata.topic();
                System.out.println("message is sent.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } finally {
            // 释放连接
            producer.close();
        }
    }
}