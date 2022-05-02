package com.chapter.twenty.one.rs.one.config;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class ProjectConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.oauth2ResourceServer(c-> {
            c.opaqueToken(t-> {
                t.introspectionUri("http://localhost:8080/oauth/check_token");
                t.introspectionClientCredentials("client1", "secret1");
            });
        });
        http.authorizeRequests().anyRequest().authenticated();
    }
}
