package com.springBoot.Postgres.Kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerConfig {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public KafkaProducerConfig(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendMessage(String topic, Object message) {
        try {
            String serializedMessage = objectMapper.writeValueAsString(message);
            kafkaTemplate.send(topic, serializedMessage);
        } catch (Exception e) {
            System.err.println("Error sending message to Kafka: " + e.getMessage());
        }
    }
}
