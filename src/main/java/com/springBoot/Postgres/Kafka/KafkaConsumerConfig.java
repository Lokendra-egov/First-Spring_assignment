package com.springBoot.Postgres.Kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerConfig {

    @KafkaListener(topics = "TopicName", groupId = "Group")
    public void listenTopic(String receivedMessage){
        System.out.println("The message received is " + receivedMessage);
    }

}
