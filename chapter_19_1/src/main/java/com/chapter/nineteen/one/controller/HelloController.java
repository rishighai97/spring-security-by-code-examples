package com.chapter.nineteen.one.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello spring security chapter 19. Resource server gets public key from authorization server and validates the token";
    }
}
