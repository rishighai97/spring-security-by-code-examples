package com.chapter.twenty.one.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableAuthorizationServer
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                .checkTokenAccess("isAuthenticated()");
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients
                .inMemory()
                .withClient("client1")
                .secret("secret1")
                .scopes("read")
                .authorizedGrantTypes("password");
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        // comment tokenStore and accessTokenConvertor to work with resource server validating opaque token
        endpoints
                .authenticationManager(authenticationManager)
                .tokenStore(tokenStore())
                .accessTokenConverter(convertor());
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(convertor());
    }

    @Bean
    public JwtAccessTokenConverter convertor() {
        var convertor = new JwtAccessTokenConverter();
        convertor.setSigningKey("asdfsdfsdfgsfgdgfghjgjgjtyjhsdcsdcsdvdfvdfvf");
        return convertor;
    }
}
