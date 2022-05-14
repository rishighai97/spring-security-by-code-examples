# Chapter 26

#### Endpoint authorization rules for an OAuth 2 resource server

## Demo (Authorization server)

### Create new project with dependencies

- spring security, spring web, spring cloud starter oauth2

### Create auth server configuration

- config package
- WebConfig
    - @Configuration
    - UserDetailsService
        - uds = new InMemoryUserDetailsManager()
        - user1 = User.withUsername("bill").password("12345").authorities("read").build()
        - uds.createUser(user1)
        - return uds
    - PasswordEncoder
        - @Bean
        - NoOpPasswordEncoder.getInstance()
    - extend WebSecurityConfigurerAdapter
    - override authManager Bean and @Bean it
- AuthConfig
    - @Configuration
    - @EnableAuthorizationServer
    - extend AuthorizationServerConfigurerADapter
    - @Autowire AuthenticationManager
    - override configure(clients)
        - clients.inMemory().withClient("client1").secret("secret1").authorizedGrantTypes("password").scopes("read")
    - override configure(endpoints)
        - endpoints.authenticationManager(authenticationManager)

### Test endpoint

- controllers package
- DemoController
- @RestController
- @GetMapping("/demo") return "Demo!"

### Test the demo endpoint

- Get the oauth token
    - http://localhost:8080/oauth/token/username=bill&password=12345&scope=read
    - Basic Auth
        - client1 | secret1
    - POST
- Hit demo endpoint with token
    - http://localhost:8080/demo
        - 401 unauthorized
        - by default all endpoints exposed by authorization server are denied (apart from oauth endpoints)
        - we need to configure authorization rules

### Configure authorization rules

- config => WebConfig
- override configure(http)
- leave super.configure at the end
    - includes configuration for oauth endpoints
- http.authorizeRequests().mvcMatchers("/demo/**").permitAll(); // will allow access without credentials

### Test the authorization server

- get token
- hit demo endpoint without token
    - 200 OK as we have permitted all requests starting with path /demo

### Generate JWT token

- config => AuthServerConfig
- TokenStore
    - @Bean TokenStore
    - return new JwtTokenStore(convertor())
    - Convertor
        - @Bean
        - c = new JwtAccessTokenConvertor()
        - c.setSigningKey("12345");
        - return c;
- override configure(endpoints)
    - endpoints.tokenStore(tokenStore()).convertor(convertor())

### Test the application

- Hit token endpoint
    - You can see the JWT token
- View token details on jwt.io
    - You can view the authorities <read>

## Demo (Resource Server 1)

### Create new project with dependencies

- spring web, spring security, spring cloud starter oauth2

### Create an endpoint for testing

- controllers package
- DemoController
- @RestController
- @GetMapping("/demo") return "Demo!"

### Configure resource server

- config package
- ResourceServerConfig
- @Configuration
- @EnableResourceServer
- extend ResourceServerConfigurerAdapter
- TokenStore
    - @Bean TokenStore
    - return new JwtTokenStore(convertor())
    - Convertor
        - @Bean
        - c = new JwtAccessTokenConvertor()
        - c.setSigningKey("12345");
        - return c;
- override configure(resources)
    - resources.tokenStore(tokenStore)

### Add new port

- resources => application.properties => server.port=9090

### Test the application with only authentication

- Generate token
- Hit /demo with token (without token gives 401 unauthorized)
    - http://localhost:9090/demo
    - Header
        - Authorization
            - Bearer <access token>

### Add authorization rule for resource server

- config => ResourceServerConfig
- Authorization rules are available in ResourceServerConfigurerAdapter class for resource server
- override configure(http)
    - remove super call
    - http.authorizeRequests().mvcMatchers("/demo/**").hasAuthority("write")
        - NOTE: <b>if for other request nothing is specified, they are permitted by default</b>

### Test the application with authority write

- Hit demo endpoint with access token
- you get 403 forbidden
- reason: token has read authority

### Add user with authority write in authorization server (Auth server project)

- config => WebConfig => uds
- user2 = User.withUsername("john").password("12345").authorities("write").build()
- uds.createUser(user2)

### Test the application with authority write

- Restart auth server
- generate token with john | 12345 | write via oauth/token endpoint
- check the token on jwt.io
- hit demo endpoint on resource server with john's access token

## Demo (Resource Server DSL method)

### Create new project with dependencies

- spring web, spring security, oauth2 resource server

### Create demo endpoint

- controller package
- DemoController
- @RestController
- @GetMapping("/demo") returns "Demo!"

### Create resource server configuration

- config package
- ResourceServerConfig
- @Configuration
- extend WebSecurityConfig
- override configure(http)
    - http.oauth2ResourceServer(
      c -> c.jwt(
      j -> j.decoder(decoder())  
      )
      )
    - JwtDecoder decoder()
        - @Bean
        - key = "asdasdsdasdsdsd.. (>36 characters)" replace the key in Auth Server=>config => AuthConfig => convertor
        - secretKey = new SecretKeySpec(key.getbytes(), 0 ,key.getBytes().length, "AES")
        - return NimbusJwtDecoder.withSecretKey(secretKey).build()

### add server.port=9090 in resource => application.properties

### Test the auth and resource server

- restart authorization and resource server
- generate access token with john
- hit demo endpoint with access token

### Add authorization rule in resource server 2

- config => ResourceServerConfig => configure(http)
- http.authorizeRequests()/mvcMatches("/demo/**").hasAuthority("read")

### Test the resource server

- Restart the resource server
- Hit demo endpoint with both bill and john (read and write authorities)
    - Generate access token and hit demo endpoint
    - We get 403 forbidden
- <b>In Resource server with DSL method, we need to tell resource server how to get token using convertor</b>

### Setup jwtAuthentication convertor. Tells how to get authorities from access token in DSL resource server

- config => ResourceServerConfig
- configure(http)
    - j.jwtAuthenticationConverter(convertor())
- Create JwtAuthenticationConverter convertor()
    - conv = new JwtAuthenticationConverter()
    - conv.setJwtGrantedAuthoritiesConverer(jwt->{ JSONArray a = (JSONArray)jwt.getClaims().get("authorities")
      return a.stream(String::valueOf).map(SimpleGrantedAuthority::new).collect(Collectors.toList())
      })

### Test the resource server

- Generate token and hit demo endpoint (with bill(read) and write(john))