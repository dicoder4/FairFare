package com.example.userservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // GET /users
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // POST /users
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userRepository.insert(user);
    }

    // GET /users/{id}
    @GetMapping("/{id}")
    public User getUserById(@PathVariable String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }
}
