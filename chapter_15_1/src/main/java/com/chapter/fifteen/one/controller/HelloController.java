package com.chapter.fifteen.one.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello Spring security chapter 15. Blackboarding database to validate access token by resource server from authorization server";
    }
}
