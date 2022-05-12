package com.chapter.thirty.security;

import com.chapter.thirty.model.Document;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DocumentMethodAuthorizationManager implements AuthorizationRuleManager {

    public boolean applySecurityPermissions(List<Document> returnedList, String authority) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        boolean docsBelongToAuthUser = returnedList
                .stream()
                .allMatch(d->d.getUser().equals(username));

        boolean hasProperAuthority = authentication.getAuthorities()
                .stream()
                .anyMatch(d->d.getAuthority().equals(authority));

        return docsBelongToAuthUser && hasProperAuthority;
    }

}
