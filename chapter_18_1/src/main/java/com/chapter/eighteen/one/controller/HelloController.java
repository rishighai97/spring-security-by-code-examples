package com.chapter.eighteen.one.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello spring security chapter 18. Using asymmetric private-public key pair for validating JWT token";
    }

}
