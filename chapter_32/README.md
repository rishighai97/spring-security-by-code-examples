# Chapter 32

#### Building an oauth2 reactive resource server

## Demo

### Start and setup a keycloack authorization server (from chapter 20)
- login to console with credentials root : rishighai
- Copy endpoints from open-id configuration
    - get token_endpoint POST
        - http://localhost:8080/auth/realms/master/protocol/openid-connect/token
    - certs endpoint jwk-uri (to view the key)
        - http://localhostL8080/auth/realms/master/protocol/openid-connect/certs
        - has the public key to be used to validate access tokens
        
- get the access token by hitting token_endpoint
    - body:
        - grant_type=password
        - client_id=example
        - username=bill
        - password=12345
        - scope=read
    - check the token on jwt.io
        - kid matches with key id in /certs endpoint
        - kid is unique per key as multiple keys can be configured
    

### Create new project iwth dependencies
- spring reative web, cloud oauth2, oauth2 resource server, spring security

### Create demo endpoint
- controllers package
- DemoController
    - @RestController
    - @GetMapping("/demo")
        - public Mono<String> demo()
        - return Mono.just("Demo!")
    
### Create project configuration
- config package
- ProjectConfig
- @Configuration
- securityWebFIlterChain(ServerHttpSecurity http)
    - @Bean
    - return http.authorizeExchange()
        .anyExchange().authenticated()
      .and().oauth2Resourver()
        .jwt(
            jwtc -> jwtc.jwkSetUri("http://localhostL8080/auth/realms/master/protocol/openid-connect/certs")   
      ).and().build();
    - NOTE:
        - jwkUri => Validate token 
        - jwtDecodes=> private key
        - publicKey=>asymmetric key in resource server code
         
### change app port
- resources => application.properties
- server.port=9090

### Test the resource server
- hit demo endpoint
    - http://localhost:9090/demo
        - 401 UNAUTHORIZED without credentials
- get access token
- Hit /demo with access token
    - Authorization
        - Bearer <access-token>
        - 200 OK Demo!
    