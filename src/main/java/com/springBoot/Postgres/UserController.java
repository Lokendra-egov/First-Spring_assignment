package com.springBoot.Postgres;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/create")
    public void createUser(@RequestBody egovUser egovUser) {
        userRepository.create(egovUser);
    }

    @GetMapping("/search")
    public List<egovUser> searchUsers(@RequestBody UserSearchCriteria criteria) {
        return userRepository.search(criteria);
//        return null;
    }

    @PutMapping("/update")
    public void updateUser(@RequestBody egovUser egovUser) {
        userRepository.update(egovUser);
    }

    @DeleteMapping("/delete")
    public void deleteUser(@RequestBody egovUser egovUser) {
        userRepository.delete(egovUser);
    }
}

