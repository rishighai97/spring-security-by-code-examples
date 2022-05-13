package com.chapter.nine.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller // mvc, always return a view
public class MainController {

    @GetMapping
    public String main() {
        return "main.html";
    }

    @PostMapping("/change")
    public String change() {
        System.out.println("Create called :(");
        return "main.html";
    }

    @PostMapping("/csrfdisabled/test")
    public String csrfDisabled() {
        System.out.println("Csrf disabled :(");
        return "main.html";
    }

}
