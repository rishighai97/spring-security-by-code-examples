package com.chapter.twenty.four.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class ProjectConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public UserDetailsService userDetailsService() {
        var inMemoryUserDetailsManager = new InMemoryUserDetailsManager();
        var userDetails1 = User.withUsername("john").password("12345").authorities("ROLE_ADMIN").build();
        var userDetails2 = User.withUsername("bill").password("12345").roles("MANAGER").build();
        inMemoryUserDetailsManager.createUser(userDetails1);
        inMemoryUserDetailsManager.createUser(userDetails2);
        return inMemoryUserDetailsManager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic();
        http.csrf().disable(); // for post endpoint
//        http
//            .authorizeRequests()
//            .anyRequest() // matcher
//            .hasRole("ADMIN"); // rule
        http
                .authorizeRequests()
                .mvcMatchers("/hello").hasRole("ADMIN") // matcher | authorization role
                .mvcMatchers("/ciao").hasRole("MANAGER")
                .anyRequest().permitAll();
    }
}
