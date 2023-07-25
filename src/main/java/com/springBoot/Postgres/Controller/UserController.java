package com.springBoot.Postgres.Controller;
import com.springBoot.Postgres.Address.AddressApiService;
import com.springBoot.Postgres.Kafka.KafkaProducerConfig;
import com.springBoot.Postgres.Repository.UserRepository;
import com.springBoot.Postgres.User.UserSearchCriteria;
import com.springBoot.Postgres.User.egovUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class UserController {

    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private AddressApiService addressApiService;
    @Autowired
    private KafkaProducerConfig producer;

    @Value("${kafka.topic.create}")
    private String topicCreate;

    @Value("${kafka.topic.update}")
    private String topicUpdate;

    @Value("${kafka.topic.delete}")
    private String topicDelete;

    public UserController(UserRepository userRepository, AddressApiService addressApiService, KafkaProducerConfig producer) {
        this.userRepository = userRepository;
        this.addressApiService = addressApiService;
        this.producer = producer;
    }

//    @GetMapping("producerMsg")
//    public void getMessageFromClient(@RequestParam("message") String message) {
//        producer.sendMessage("topicCreate", message);
//    }

    @PostMapping("create")
    public void createUser(@RequestBody List<egovUser> egovUsers) {
        for (egovUser egovUser : egovUsers) {
            if (isDuplicateUser(egovUser)) {
                System.out.println("This user already exists: " + egovUser.getName() + " - " + egovUser.getMobileNumber());
                continue;
            }
            UUID id = UUID.randomUUID();
            egovUser.setId(id);
            String address = addressApiService.getAddressFromApi();
            producer.sendMessage("topicCreate", egovUser);
            userRepository.create(egovUser,address);
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

    @PostMapping("search")
    public List<egovUser> searchUsers(@RequestBody UserSearchCriteria criteria) {
        return userRepository.search(criteria);
    }

    public void updateUser(@RequestBody List<egovUser> egovUsers) {
        for (egovUser egovUser : egovUsers) {
            producer.sendMessage("topicUpdate", egovUser);
            userRepository.update(egovUser);
        }
    }

    @GetMapping("_getall")
    public List<egovUser> getAllUsers() {
        return userRepository.getAllUsers();
    }

    @DeleteMapping("delete")
    public void deleteUser(@RequestBody egovUser egovUser) {
        producer.sendMessage("topicDelete", egovUser);
        userRepository.delete(egovUser);
    }
}

