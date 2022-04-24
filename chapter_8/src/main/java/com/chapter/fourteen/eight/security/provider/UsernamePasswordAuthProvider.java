package com.chapter.fourteen.eight.security.provider;

import com.chapter.fourteen.eight.security.authentications.UsernamePasswordAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UsernamePasswordAuthProvider implements AuthenticationProvider {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var username = authentication.getName();
        var password = String.valueOf(authentication.getCredentials());
        var userDetails = userDetailsService.loadUserByUsername(username);
        if (passwordEncoder.matches(password, userDetails.getPassword())) {
            return new UsernamePasswordAuthentication(username, password, userDetails.getAuthorities());
        }
        throw new BadCredentialsException("Invalid credentials");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthentication.class.equals(authentication);
    }
}
