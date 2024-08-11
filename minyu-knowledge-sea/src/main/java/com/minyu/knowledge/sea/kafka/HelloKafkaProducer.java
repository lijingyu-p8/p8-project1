package com.minyu.knowledge.sea.kafka;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
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

    public void sendData() throws ExecutionException, InterruptedException {
        // 配置属性集合
        Map<String, Object> configMap = new HashMap<>();
        //  配置属性：Kafka服务器集群地址
        configMap.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        //  配置属性：Kafka生产的数据为KV对，所以在生产数据进行传输前需要分别对K,V进行对应的序列化操作
        configMap.put(
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                org.apache.kafka.common.serialization.StringSerializer.class);
        configMap.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                org.apache.kafka.common.serialization.StringSerializer.class);
        configMap.put(
                ProducerConfig.INTERCEPTOR_CLASSES_CONFIG,
                List.of(KafkaInterceptorMock.class));
        configMap.put(ProducerConfig.PARTITIONER_CLASS_CONFIG,KafkaPartitionerMock.class);
        //  创建Kafka生产者对象，建立Kafka连接
        //      构造对象时，需要传递配置参数
        KafkaProducer<String, String> producer = new KafkaProducer<>(configMap);
        //  准备数据,定义泛型
        //  构造对象时需要传递 【Topic主题名称】，【Key】，【Value】三个参数
        ProducerRecord<String, String> record = new ProducerRecord<String, String>(
                "test", "key1", "value1"
        );
        record = new ProducerRecord<String, String>(
                "test", 1, "key1", "value1"
        );
        //  生产（发送）数据
        producer.send(record, new Callback() {
            @Override
            public void onCompletion(RecordMetadata recordMetadata, Exception e) {

            }
        });
        Future<RecordMetadata> send = producer.send(record);
        RecordMetadata recordMetadata = send.get();
        int partition = recordMetadata.partition();
        String topic = recordMetadata.topic();
        //  关闭生产者连接
        producer.close();
    }
}