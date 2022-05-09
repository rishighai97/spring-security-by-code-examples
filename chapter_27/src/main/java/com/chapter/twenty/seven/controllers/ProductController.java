package com.chapter.twenty.seven.controllers;

import com.chapter.twenty.seven.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/products/{username}")
    public List<String> products(@PathVariable String username) {
        return productService.findProductsByUser(username);
    }

}
