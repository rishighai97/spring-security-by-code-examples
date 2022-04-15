package com.chapter.five.security.provider;

import com.chapter.five.security.authentication.CustomAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Value("${key}")
    private String key;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var requestHeaderPrincipal = authentication.getName();
        if (key.equals(requestHeaderPrincipal)) {
            var authenticationResult = new CustomAuthentication(null, null, null);
            return authenticationResult;
        } else {
            throw new BadCredentialsException("Error");
        }
    }

    @Override
    public boolean supports(Class<?> authenticationType) {
        return CustomAuthentication.class.equals(authenticationType);
    }
}
