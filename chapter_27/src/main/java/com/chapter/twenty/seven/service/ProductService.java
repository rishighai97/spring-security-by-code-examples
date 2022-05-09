package com.chapter.twenty.seven.service;

import org.springframework.context.annotation.Bean;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
//@PostAuthorize("hasAuthority('write')")
@PostAuthorize("#username==authentication.name")
public class ProductService {


    public List<String> findProductsByUser(String username) {
        return List.of("beer", "chocolate");
    }

}
