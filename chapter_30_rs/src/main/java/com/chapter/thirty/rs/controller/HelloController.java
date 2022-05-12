package com.chapter.thirty.rs.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize("hasAuthority('read')")
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello spring security chapter 19. Resource server gets public key from authorization server and validates the token";
    }

}
