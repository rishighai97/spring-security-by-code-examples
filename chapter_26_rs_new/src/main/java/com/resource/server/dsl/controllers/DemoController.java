package com.resource.server.dsl.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @GetMapping("/demo")
    public String demo() {
        return "Demo!";
    }

    @GetMapping("/other")
    public String other() {
        return "Other endpoint!";
    }

}