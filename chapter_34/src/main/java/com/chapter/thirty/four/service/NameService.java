package com.chapter.thirty.four.service;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class NameService {

    @PreAuthorize("hasAuthority('read')")
    public String getName() {
        return "Mary";
    }

}
