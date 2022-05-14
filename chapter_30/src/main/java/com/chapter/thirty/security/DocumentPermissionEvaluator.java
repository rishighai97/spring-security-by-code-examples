package com.chapter.thirty.security;


import com.chapter.thirty.model.Document;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import java.io.Serializable;
import java.util.List;

public class DocumentPermissionEvaluator implements PermissionEvaluator {
    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {

        String username = authentication.getName();
        List<Document> returnedList = (List<Document>) targetDomainObject;
        boolean docsBelongToAuthUser = returnedList
                .stream()
                .allMatch(d -> d.getUser().equals(username));

        String authority = (String) permission;
        boolean hasProperAuthority = authentication.getAuthorities()
                .stream()
                .anyMatch(d -> d.getAuthority().equals(authority));

        return docsBelongToAuthUser && hasProperAuthority;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return false;
    }
}
