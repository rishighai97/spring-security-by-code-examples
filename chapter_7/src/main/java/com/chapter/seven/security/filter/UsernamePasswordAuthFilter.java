package com.chapter.seven.security.filter;

import com.chapter.seven.entity.Otp;
import com.chapter.seven.repository.OtpRepository;
import com.chapter.seven.security.authentications.OtpAuthentication;
import com.chapter.seven.security.authentications.UsernamePasswordAuthentication;
import com.chapter.seven.security.mangers.TokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

@Component
public class UsernamePasswordAuthFilter extends OncePerRequestFilter {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private TokenManager tokenManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var username = request.getHeader("username");
        var password = request.getHeader("password");
        var otp = request.getHeader("otp");
        if (Objects.isNull(otp)) {
            // step 1: Check if username and password is valid and then generate and store otp in database
            var authenticated = authenticationManager.authenticate(new UsernamePasswordAuthentication(username, password));
            if (authenticated.isAuthenticated()) {
                Otp otpEntity = new Otp();
                otpEntity.setUsername(username);
                otpEntity.setOtp(String.valueOf(new Random().nextInt(9999)));
                otpRepository.save(otpEntity);
            } else {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            }
        } else {
            // step 2" Get and validate otp from database and return token if valid
            var authenticated = authenticationManager.authenticate(new OtpAuthentication(username, otp));
            if (authenticated.isAuthenticated()) {
                var uuid = UUID.randomUUID().toString();
                tokenManager.add(uuid);
                response.setHeader("Authorization", uuid);
            } else {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            }
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return !request.getServletPath().equals("/login");
    }
}
