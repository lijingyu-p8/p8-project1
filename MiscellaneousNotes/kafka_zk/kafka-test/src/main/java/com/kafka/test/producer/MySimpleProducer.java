package com.kafka.test.producer;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

public class MySimpleProducer {
    private final static String TOPIC_NAME = "first-topic";

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        //1.设置参数
        Properties properties = new Properties();
        //设置Kafka地址
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.73.101:9092,192.168.73.102:9092,192.168.73.103:9092");
        //把发送的key从字符串序列化为字节数组
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        //把发送消息value从字符串序列化为字节数组
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.ACKS_CONFIG, "1");
        /*
        发送失败会重试，默认重试间隔100ms，重试能保证消息发送的可靠性，但是也可能造
        成消息重复发送，⽐如⽹络抖动，所以需要在
        接收者那边做好消息接收的幂等性处理
         */
        properties.put(ProducerConfig.RETRIES_CONFIG, 3);
        //重试间隔设置
        properties.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, 300);
        //2.创建⽣产消息的客户端，传⼊参数
        Producer<String, String> producer = new KafkaProducer<String, String>(properties);
        //key：作⽤是决定了往哪个分区上发， value：具体要发送的消息内容
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>(TOPIC_NAME, "mykeyvalue", "hellokafka");
        //发送到指定的分区
        ProducerRecord<String, String> partisionRecord = new ProducerRecord<>(TOPIC_NAME, 0, "key1", "value1");
//        //4.发送消息,得到消息发送的元数据并输出
//        RecordMetadata metadata = producer.send(producerRecord).get();
//        System.out.println("同步⽅式发送消息结果： " + "topic-" +
//                metadata.topic() + "|partition-" + metadata.partition() + "|offset-" + metadata.offset());
        //异步发送
        producer.send(producerRecord, new Callback() {
            @Override
            public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                System.out.println("异步⽅式发送消息结果： " + "topic-" +
                        recordMetadata.topic() + "|partition-" + recordMetadata.partition() + "|offset-" + recordMetadata.offset());
            }
        });
        countDownLatch.await();
    }
}
