package com.chapter.twenty.nine.service;

import com.chapter.twenty.nine.model.Document;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentService {

    //    @PostAuthorize("hasPermission(returnObject, 'read')") // can also pass '' or null as the permission. Authentication object is passed by default
    @PostAuthorize("@documentMethodAuthorizationManager.applySecurityPermissions(returnObject, 'read')")
    public List<Document> findDocuments(String username) {
        var doc = new Document();
        doc.setUser("john");
        doc.setText("TEXT");
        return List.of(doc);
    }

}
