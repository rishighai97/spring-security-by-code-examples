package com.chapter.twenty.eight.controller;

import com.chapter.twenty.eight.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class DemoController {

    @Autowired
    private DemoService demoService;

    @GetMapping("/test1")
    public String test1() {
        return demoService.test1();
    }

    @GetMapping("/test2")
    public String test2() {
        return demoService.test2();
    }

    @GetMapping("/test3")
    public String test3() {
        return demoService.test3();
    }

    @GetMapping("/test4")
    public List<String> test4() {
        List<String> list = new ArrayList<>();
        list.add("bill");
        list.add("john");
        list.add("mary");
        return demoService.test4(list);
    }

    @GetMapping("/test5")
    public List<String> test5() {
        List<String> list = List.of("bill", "john", "mary");
        return demoService.test4(list);
    }

    @GetMapping("/test6")
    public List<String> test6() {
        return demoService.test6();
    }
}
