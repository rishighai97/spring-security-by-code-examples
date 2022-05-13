package com.chapter.six.security.provider;

import com.chapter.six.entity.Otp;
import com.chapter.six.repository.OtpRepository;
import com.chapter.six.security.authentications.OtpAuthentication;
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
