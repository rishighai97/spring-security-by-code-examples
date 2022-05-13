package com.chapter.eight.controller;

import org.springframework.security.concurrent.DelegatingSecurityContextExecutorService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//@Async // not applicable when not using initializing bean with Inheritable Thread Local / Defining your own thread pool for handling security context
@RestController
public class HelloController {
    @GetMapping("/hello")
    public String hello() { //Authentication authentication
        Runnable runnable = () -> {
            var authentication = SecurityContextHolder.getContext().getAuthentication();
            System.out.println(authentication);
        };
//        DelegatingSecurityContextRunnable delegatingSecurityContextRunnable = new DelegatingSecurityContextRunnable(runnable);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
//        executorService.submit(runnable);
//        executorService.shutdown();
        DelegatingSecurityContextExecutorService delegatingSecurityContextExecutor = new DelegatingSecurityContextExecutorService(executorService);
        delegatingSecurityContextExecutor.submit(runnable);
        delegatingSecurityContextExecutor.shutdown();
        return "Chapter 8. Logged in";
    }
}