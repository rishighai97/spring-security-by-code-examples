package com.chapter.twenty.five.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
        var uds = new InMemoryUserDetailsManager();
        var userDetails1 = User.withUsername("bill").password("12345").authorities("read").build();
        var userDetails2 = User.withUsername("john").password("12345").authorities("write").build();
        uds.createUser(userDetails1);
        uds.createUser(userDetails2);
        return uds;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic();

        // mvc matcher

        // scenario 1: /a with bill
//        http
//                .authorizeRequests()
//                .mvcMatchers("/a").hasAuthority("read")
//                .anyRequest().denyAll();

        // scenario 2: anything after /a works with bill
//        http
//                .authorizeRequests()
//                .mvcMatchers("/a/**").hasAuthority("read")
//                .anyRequest().denyAll();

        // scenario 3: only /b/c works with /* for john | to enable /b or /b/c/<x> add another mvc matcher /b
//        http
//                .authorizeRequests()
//                .mvcMatchers("/a/**").hasAuthority("read")
//                .mvcMatchers("/b/*").hasAuthority("write") // .mvcMatchers("/b").authenticated() // for /b or /b/c/<x> to work
//                .anyRequest().denyAll();

        // scenario 4: /c/{name} works with any authenticated user
//        http
//                .authorizeRequests()
//                .mvcMatchers("/a/**").hasAuthority("read")
//                .mvcMatchers("/b/*").hasAuthority("write")
//                .mvcMatchers("/b").authenticated()
//                .mvcMatchers("/c/{name}").authenticated()
//                .anyRequest().denyAll();


        // ant matcher

        // scenario 1: /a/** works same as mvc matcher
//        http
//                .authorizeRequests()
//                .antMatchers("/a/**").hasAuthority("read")
//                .anyRequest().denyAll();

        // scenario 2 a: /a works but /a/ is permitted
//        http
//                .authorizeRequests()
//                .antMatchers("/a").hasAuthority("read")
//                .anyRequest().permitAll();

        // scenario 2 b: fixing scenario 2 issue with mvcMatcher
//        http
//                .authorizeRequests()
//                .mvcMatchers("/a").hasAuthority("read")
//                .anyRequest().permitAll(); // works when no creds are passed. If creds are passed then authentication filter comes into picture and does the authentication

        // scenario 3: adding http method
        http
                .authorizeRequests()
                .mvcMatchers(HttpMethod.GET, "/a").hasAuthority("read")
                .anyRequest().permitAll();

    }
}
