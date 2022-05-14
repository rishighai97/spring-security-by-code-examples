package com.chapter.thirty.controllers;


import com.chapter.thirty.model.Document;
import com.chapter.thirty.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @GetMapping("/document/{username}")
    // this example did not need username as path variable since we use username from authenticated user
    public List<Document> findDocuments(@PathVariable String username) {
        return documentService.findDocuments(username);
    }

}
