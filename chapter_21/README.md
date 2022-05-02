# Chapter 21

#### Using DSL (new) method to implement the resource server

## Demo : Create a simple authorization server

### Configurations
- config package
- UserManagementConfig
    - UserDetailsService
    - PasswordEncoder
    - AuthenticationManager
- AuthServerConfig
    - configure(clients)
        - client1 | secret1 | read | password 
    - configure(endpoints)
        - authentication manager
    - configure(security)
        - checkTokenAccess : isAuthenticated()
- user opaque tokens

### Demo via postman client
- Get access token
    - http://localhost:8080/oauth/token
        - body
            - grant_type=password
            - scope = read
            - username=john
            - password = 12345
        - Auth headerclient_id = client1 client_secret=secret1
- check token introspection endpoint
    - http://localhost:8080/oauth/check_token?token=<access-token>
        - body: token | <access-token>
- You can view the opaque token

## Demo : Implementing resource server using DSL method (new project)

### Create a new project with required dependencies
- spring web, spring security, oauth2 resource server
- Add runtime dependency for com.nimbusds:oauth2-oidc-sdk (v>=8.4)
    - Needed as a manager for token introspection in resource server

### change server port
- resources => application.properties
- server.port=9090

### Add endpoint to test the resoruce server
- controller package
- HelloController
    - @RestController
    - @GetMapping("/hello")
        - Returns String "Hello!"

### Add configurations
- config package
- ProjectConfig
    - @Configuration
    - You do not have @EnableResourceServer
- extend WebSecurityConfigurerAdapter
- override configure(http)
    - http.oauth2ResourceServer(
      
      ) // configure resource server
        - Configure the customizer
            - c -> c.opaqueToken(
              )
                - t-> 
                    - t.introspectionUri("http://localhost:8080/oauth/check_token")
                    - t.introspectionClientCredentials("client1", "secret1")
    - Authenticated requests should only be allowed
        - http.authorizeRequests().anyRequest().authenticated()
    
### Test the resource server
- Generate access token
- Hit hello endpoint
    - http://localhost:9090/hello
    - Header
        - Authorization | Bearer <access-token>
- You can view the response "Hello!"

## Demo: Replacing opque token with JWT in auth server and resource server

## Update authorization server to get JWT

### Add token store
- config
- AuthServerConfig
- Token Store
    - @Bean
    - new JwtTokenStore(convertor())
- Token Convertor
    - @Bean
    - c = new JwtAccessTokenConvertor()
    - c.setSigningKey("asdsdfsdfsdfasdfdsfd") // add large number of bytes as a cryptographic key with specific size is expected
    - return c
- Add token store and convertor in configure(endpoints)
    - endpoints.tokenStore(tokenStore()).convertor(convertor())
    
### Test the application
- Hit oauth/token
    - You can view the JWT token

## Create new resource server using JWT token

### create new project with dependencies
- spring web, spring security, oauth2 resource server
- Add runtime dependency for com.nimbusds:oauth2-oidc-sdk (v>=8.4)
    - Needed as a manager for token introspection in resource server


### Get Controller from previous resource server

### Run server on port 9090
- resource => application.properties => server.port=9090

### Add project config
- config package
- ProjectConfig
    - @Configuration
    - extend WebSecurityConfigurerAdapter
    - override configure(HttpSecurity http)
        - http.oauth2ResourceServer(c->)
            - c->c.jwt(j->)
                - j -> j.decoder(decoder())
                - define decoder()
                    - @Bean
                    - JwtDecoder
                    - NimbusJwtDecoder 
                        - Has 3 methods
                            - withSecretKey (symmetric key)
                            - withPublicKey (asymmetric key)
                            - withJwkSetUri (third party authorization server like keyCloack)
                        - String keyValue = <asymmetric key from authorization server>
                        - SecretKey secretKey = new SecretKeySpec(keyValue.getBytes(), 0, keyValue.getByter().length(), "AES")
                        - return NimbusJwtDecoder.withSecretKey(secretKey).build()
        - http.authorizeRequests().anyRequest().authenticated()
    
### Test the resource server
- Generate JWT access token
- Hit hello endpoint with JWT acceess token