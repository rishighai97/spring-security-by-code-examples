package com.chapter.seventeen.one.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello Spring security chapter 17. Using symmetric key to validate JWT authentication token at resource server";
    }
}
