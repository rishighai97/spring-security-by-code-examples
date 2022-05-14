package com.resource.server.dsl.config;

import com.nimbusds.jose.shaded.json.JSONArray;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.stream.Collectors;

@Configuration
public class ResourceServerConfig extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .oauth2ResourceServer(c -> {
                    c.jwt(j -> {
                        j.decoder(decoder());
                        j.jwtAuthenticationConverter(convertor());
                    });
                });
        // todo check why authentication ignored when not passing bearer token (seems like issue with postman. Working now)
        http
                .authorizeRequests()
                .mvcMatchers("/demo/**").hasAuthority("read")
                .anyRequest().authenticated();
    }

    @Bean
    public JwtDecoder decoder() {
        var key = "asdjfkjfbjkewbfjkewbfjkegkbjkgbjkerbgkjergr";
        SecretKey secretKey = new SecretKeySpec(key.getBytes(), 0, key.getBytes().length, "AES");
        return NimbusJwtDecoder.withSecretKey(secretKey).build();
    }

    @Bean
    public JwtAuthenticationConverter convertor() {
        var conv = new JwtAuthenticationConverter();
        conv.setJwtGrantedAuthoritiesConverter(c -> {
            JSONArray jsonArray = (JSONArray) c.getClaims().get("authorities");
            return jsonArray
                    .stream()
                    .map(String::valueOf)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        });
        return conv;
    }
}
