package com.chapter.fourteen.seven.controller;

import org.springframework.security.concurrent.DelegatingSecurityContextExecutorService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
public class HelloController {
    @GetMapping("/hello")
    public String hello() {
        return "Chapter 7. Logged in";
    }
}