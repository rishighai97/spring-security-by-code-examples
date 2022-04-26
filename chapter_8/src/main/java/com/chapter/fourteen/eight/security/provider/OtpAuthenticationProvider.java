package com.chapter.fourteen.eight.security.provider;


import com.chapter.fourteen.eight.entity.Otp;
import com.chapter.fourteen.eight.repository.OtpRepository;
import com.chapter.fourteen.eight.security.authentications.OtpAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class OtpAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private OtpRepository otpRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        var username = authentication.getName();
        var otp = String.valueOf(authentication.getCredentials());

        Optional<Otp> otpOptional = otpRepository.findOtpByUsername(username);
        if (otpOptional.isPresent() && otpOptional.get().getOtp().equals(otp)) {
            return new OtpAuthentication(username, otp, authentication.getAuthorities());
        }
        throw new BadCredentialsException("Invalid Credentials");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return OtpAuthentication.class.equals(authentication);
    }
}