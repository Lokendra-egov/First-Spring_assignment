package com.springBoot.Postgres.Kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springBoot.Postgres.Repository.UserRepository;
import com.springBoot.Postgres.User.UserSearchCriteria;
import com.springBoot.Postgres.User.egovUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KafkaConsumerConfig {

    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public KafkaConsumerConfig(UserRepository userRepository, ObjectMapper objectMapper) {
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "topicCreate", groupId = "group1")
    public void listenTopicCreate(String receivedMessage) {
        try {
            egovUser user = objectMapper.readValue(receivedMessage, egovUser.class);
            if (isDuplicateUser(user)) {
                System.out.println("This user already exists: " + user.getName() + " - " + user.getMobileNumber());
                return;
            }
            System.out.println("User created: " + user.getName() + " - " + user.getMobileNumber());
        } catch (Exception e) {
            System.err.println("Error processing create message: " + e.getMessage());
        }
    }

    @KafkaListener(topics = "topicUpdate", groupId = "group1")
    public void listenTopicUpdate(String receivedMessage) {
        try {
            egovUser user = objectMapper.readValue(receivedMessage, egovUser.class);
            userRepository.update(user);
            System.out.println("User updated: " + user.getName() + " - " + user.getMobileNumber());
        } catch (Exception e) {
            System.err.println("Error processing update message: " + e.getMessage());
        }
    }

    @KafkaListener(topics = "topicDelete", groupId = "group1")
    public void listenTopicDelete(String receivedMessage) {
        try {
            egovUser user = objectMapper.readValue(receivedMessage, egovUser.class);
            userRepository.delete(user);
            System.out.println("User deleted: " + user.getName() + " - " + user.getMobileNumber());
        } catch (Exception e) {
            System.err.println("Error processing delete message: " + e.getMessage());
        }
    }

    private boolean isDuplicateUser(egovUser newUser) {
        UserSearchCriteria criteria = new UserSearchCriteria(null, newUser.getMobileNumber(), true);
        List<egovUser> existingUsers = userRepository.search(criteria);
        for (egovUser existingUser : existingUsers) {
            if (existingUser.getName().equals(newUser.getName())) {
                return true;
            }
        }
        return false;
    }
}
