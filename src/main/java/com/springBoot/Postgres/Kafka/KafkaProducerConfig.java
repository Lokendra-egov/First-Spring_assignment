package com.springBoot.Postgres.Kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerConfig {

    @Autowired
    static
    KafkaTemplate<String, String> kafkaTemplate;

    public static void sendMsgToTopic(String message){
        kafkaTemplate.send("TopicName", message);
    }
}
