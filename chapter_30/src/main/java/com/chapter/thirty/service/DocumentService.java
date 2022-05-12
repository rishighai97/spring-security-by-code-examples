package com.chapter.thirty.service;

import com.chapter.thirty.model.Document;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Service;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@Service
public class DocumentService {

//    @Secured("ROLE_MANAGER")
    @RolesAllowed("ROLE_MANAGER")
    public List<Document> findDocuments(String username) {
        var doc = new Document();
        doc.setUser("john");
        doc.setText("TEXT");
        return List.of(doc);
    }

}
