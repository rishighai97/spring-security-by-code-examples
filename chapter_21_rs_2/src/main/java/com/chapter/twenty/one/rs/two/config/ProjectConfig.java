package com.chapter.twenty.one.rs.two.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Configuration
public class ProjectConfig extends WebSecurityConfigurerAdapter {



    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.oauth2ResourceServer(c->{
            c.jwt(j->{
                JwtDecoder decoder = decoder();
                j.decoder(decoder);
            });
        });
        http.authorizeRequests().anyRequest().authenticated();
    }

    @Bean
    public JwtDecoder decoder() {
        String keyValue = "asdfsdfsdfgsfgdgfghjgjgjtyjhsdcsdcsdvdfvdfvf";
        SecretKey secretKey = new SecretKeySpec(keyValue.getBytes(), 0, keyValue.length() ,"AES");
        return NimbusJwtDecoder.withSecretKey(secretKey).build();
    }
}
