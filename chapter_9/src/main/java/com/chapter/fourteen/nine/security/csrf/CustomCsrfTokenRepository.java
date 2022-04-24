package com.chapter.fourteen.nine.security.csrf;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.DefaultCsrfToken;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CustomCsrfTokenRepository implements CsrfTokenRepository {
    @Override
    public CsrfToken generateToken(HttpServletRequest request) {
        CsrfToken csrfToken = new DefaultCsrfToken("X-CSRF", "_csrf", "123456789"); // parameter name is set in httpServletRequest.attribute
        return csrfToken;
    }

    @Override
    public void saveToken(CsrfToken token, HttpServletRequest request, HttpServletResponse response) {

    }

    @Override
    public CsrfToken loadToken(HttpServletRequest request) {
        return null;
    }
}
