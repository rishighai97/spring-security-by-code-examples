package com.chapter.fourteen.eight.security.provider;

import com.chapter.fourteen.eight.security.authentications.TokenAuthentication;
import com.chapter.fourteen.eight.security.mangers.TokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class TokenAuthProvider implements AuthenticationProvider {

    @Autowired
    private TokenManager tokenManager;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var token = authentication.getName();
        var exists = tokenManager.contains(token);
        if (exists) {
            return new TokenAuthentication(token, null, null);
        }
        throw new BadCredentialsException("Invalid token");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return TokenAuthentication.class.equals(authentication);
    }
}
