package com.resource.server.legacy.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @GetMapping("/demo")
    public String demo() {
        return "Demo!";
    }

    @GetMapping("/noauth")
    public String noauth() {
        return "No Auth!";
    }

}
