package com.chapter.fourteen.six.config;

import com.chapter.fourteen.six.security.filter.UsernamePasswordAuthFilter;
import com.chapter.fourteen.six.security.provider.OtpAuthenticationProvider;
import com.chapter.fourteen.six.security.provider.UsernamePasswordAuthProvider;
import com.chapter.fourteen.six.service.JpaUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
public class ProjectConfig extends WebSecurityConfigurerAdapter {

    @Lazy
    @Autowired
    private UsernamePasswordAuthFilter usernamePasswordAuthFilter;

    @Lazy
    @Autowired
    private UsernamePasswordAuthProvider usernamePasswordAuthProvider;

    @Lazy
    @Autowired
    private OtpAuthenticationProvider otpAuthenticationProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterAt(usernamePasswordAuthFilter, BasicAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.authenticationProvider(usernamePasswordAuthProvider).authenticationProvider(otpAuthenticationProvider);
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new JpaUserDetailsService();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
