package com.minyu.knowledge.sea.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class SpecificOffsetConsumer {

    public static void main(String[] args) {
        // 设置消费者配置
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "test-group");
        props.put("enable.auto.commit", "false"); // 关闭自动提交偏移量
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        // 创建消费者
        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)) {
            // 指定要消费的topic
            String topic = "your-topic";
            // 指定分区和偏移量
            int partition = 0; // 示例分区
            long offset = 100L; // 示例偏移量

            // 分配分区
            consumer.assign(Collections.singletonList(new org.apache.kafka.common.TopicPartition(topic, partition)));

            // 查找分区最新偏移量（可选，用于验证或调试）
            long endOffset = consumer.endOffsets(Collections.singleton(new org.apache.kafka.common.TopicPartition(topic, partition))).get(new org.apache.kafka.common.TopicPartition(topic, partition));

            // 寻求到指定偏移量
            consumer.seek(new org.apache.kafka.common.TopicPartition(topic, partition), offset);

            // 循环消费消息
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, String> record : records) {
                    System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
                }

                // 根据实际情况处理偏移量提交（例如，每处理N条消息后）
                // consumer.commitSync();

                // 根据需要添加退出条件
            }
        }
    }
}
