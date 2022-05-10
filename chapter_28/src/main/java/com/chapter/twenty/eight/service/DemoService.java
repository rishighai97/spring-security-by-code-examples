package com.chapter.twenty.eight.service;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DemoService {

    @PreAuthorize("hasAuthority('read')")
    public String test1() {
        return "TEST1";
    }

    @PostAuthorize("hasAuthority('read')")
    public String test2() {
        System.out.println("TEST2");
        return "TEST2";
    }

    @PostAuthorize("returnObject == authentication.name")
    public String test3() {
        System.out.println("Getting data from database");
        var dbData = "john";
        return dbData;
    }

    @PreFilter("filterObject != authentication.name")
    public List<String> test4(List<String> list) {
        System.out.println(list);
        return list;
    }

    @PostFilter("filterObject != authentication.name")
    public List<String> test6() {
        List<String> list = new ArrayList<>();
        list.add("bill");
        list.add("john");
        list.add("mary");
        System.out.println(list);
        return list;
    }

}
