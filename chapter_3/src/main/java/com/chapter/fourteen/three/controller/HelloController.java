package com.chapter.fourteen.three.controller;

import com.chapter.fourteen.three.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Autowired
    private JdbcUserDetailsManager userDetailsManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/hello")
    public String hello() {
        return "hello spring security chapter 3";
    }

    @PostMapping("/user")
    public String addUser(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDetailsManager.createUser(user);
        return "User added successfully";
    }

}
