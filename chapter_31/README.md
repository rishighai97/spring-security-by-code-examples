# Chapter 31

#### Securing reactive apps

- In reactive app you do not have one thread per request
- you have a pool of threads
- when client requests, you place the request as a Task in the pool
- the task is completed by some threads from the pool
- SecurityContext is per thread
- In reactive app, this was not possible and hence was re implemented

## Demo

### Create new project with dependencies

- spring security, spring reactive web

### Create controller

- controller package
- DemoController
- @RestController
- Endpoint
    - Mono<String> demo() // reactive producer
    - @GetMapping("/demo")
    - return Mono.just("Demo!")

### Change server port

- resource => applicatio.properties
- server.port=9090

### Test the application as is to view default security configuration

- You can view the generated password (same as non reactive spring security application)
- Created by in memory user details manager having user "user" and password <generated-password>
- Reactive app starts on netty
- Call endpoint without auth
    - http://localhost:9090/demo
    - 401 Unauthorized
- Call endpoint with auth
    - user | <generated-password>
    - 200 OK Demo!

### Create Authentication configurations

- config package
- ProjectConfig
- @Configuration
- ReactiveUserDetailsService
    - @Bean
    - NOTE:
        - Contract is almost same as non reactive one, just the return type has Mono publisher
    - user1 = User.withUsername("john").password("12345").authorities("read").build()
    - user2 = User.withUsername("bill").password("12345").authorities("write").build()
    - return new MapReactiveUserDetailsService(user1, user2) // analogous to inMemoryUserDetailsManager
- PasswordEncoder
    - return NoOpPasswordEncoder.getInstance()
- Test the application with httpBasic credentials
    - /demo | john | 12345

### Create endppoint authorization configuration

- no need to extend WebSecurityConfigurerAdapter
- SecurityWebFilterChain(ServerHttpSecurity http) // Equivalent to configure(http) from WebSecurityConfigurerAdapter
    - @Bean (automatically injected by spring in the context)
    - http .authorizeExchange() // analogous to authorizeRequests()
      // .pathMatchers() // equivalent of mvcMatchers .anyExchange().hasAuthority("read") // equivalent to anyRequest
      .and().httpBasic()
      .and().build()
- can configure cors, csrf, oauth, etc
- Test the application via postman
    - Only john should be able to access
    - /demo | john | 12345 | read
        - 200 OK
    - /demo | bill | 12345 | write
        - 403 FORBDIDDEN

### Getting authentication object from context

- controller => DemoController
- demo(Mono<Authentication> monoAuthentication) // You get a mono producer
    - Alternative:
        - Mono<Authentication> monoAuthentication = ReactiveSecurityContextHolder .getContext()
          .map(sc->sc.getAuthentication()); // equivalent to SecurityContextHolder for the reactive thread
    - return monoAuthentication.map(authentication -> "Hello, ".concat(authentication.getName))
- Test the application via postman
    - /demo | john | 12345 | read
        - 200 OK
        - Hello, john

### NOTE

- No Authentication provider in reactive spring
- Authentication manager handles authentication

### Method security configuration (PreAuthorize)

- Not as mature as the Non Reactive app
- @PreFilter and @PostFilter do not work with Reactive Method level security
- Enable reactive global method security
    - config => ProjectCOnfig
    - @EnableReactiveGlobalMethodSecurity
        - No option to choose from prePostEnable, secured, jsr350
    - Disable endpoint security for testing
        - Replace .hasAuthority('read') with .authenticated()
- Add @PreAuthorize
    - controller => DemoController
    - @PreAuthorize("hasAuthority('write')")

- Test the application
    - /demo | john | 12345 | read => 403
    - /demo | bill | 12345 | write => 200