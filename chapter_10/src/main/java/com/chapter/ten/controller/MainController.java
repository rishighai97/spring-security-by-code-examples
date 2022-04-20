package com.chapter.ten.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
//@CrossOrigin("*")
public class MainController {

    @GetMapping("/")
    public String main() {
        return "main.html";
    }

    @PostMapping("/test")
    @ResponseBody
//    @CrossOrigin("*")
    public String test() {
        System.out.println(":("); // called even with CORS error
        return "TEST!"; // Not returned on CORS error
    }

}
