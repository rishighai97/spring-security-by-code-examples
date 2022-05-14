package com.chapter.twenty.three.config;

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
        var userDetails1 = User.withUsername("bill").password("12345").authorities("manager").build(); // should be ROLE_manager
        var userDetails2 = User.withUsername("john").password("12345").authorities("admin").build(); // should be ROLE_admin
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

        http
                .authorizeRequests() // authorization starts
                .anyRequest() // matcher
//            .permitAll() // rule
//            .denyAll() // rule
//            .hasAuthority("manager") // rule
                .hasAnyAuthority("admin", "manager") // rule | should be ROLE_admin, ROLE_manager
        ;

    }
}
