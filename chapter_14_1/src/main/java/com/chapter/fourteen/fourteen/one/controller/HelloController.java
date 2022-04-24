package com.chapter.fourteen.fourteen.one.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello Spring security chapter 14. Resource server accessible via the token";
    }
}
