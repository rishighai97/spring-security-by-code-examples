# Chapter 24

#### Authorities, Roles and Matcher methods

## Demo

### Create new project with dependencies
- spring web, spring security

### Create endpoint 
- controller package
- HelloController
- @RestController
- @GetMapping("/hello") returning "Hello!"

### Create configuration
- config package
- ProjectConfig
- @Configuration
- UserDetailsService
    - inMemoryUserDetailsManager = new InMemoryUserDetailsManager()
    
    - userDetails1 = User.withUsername("john").password("12345").authorities("ADMIN").build()
    - userDetails2 = User.withUsername("bill").password("12345").authorities("MANAGER").build()
    - inMemoryUserDetailsManager.createUser(userDetails1)
    - inMemoryUserDetailsManager.createUser(userDetails2) 
    - return inMemoryUserDetailsManager
- PasswordEncoder
    - @Bean
    - NoOpPasswordEncover.getInstance()

### Setup authorization rules with hasRole authorization rule
- config => ProjectConfig
- extend WebSecurityConfigurerAdapter
- override configure(security)
- http.httpBasic()
- Rules
    - hasAuthority()
    - hasAnyAuthority()
    - hasRole()
    - hasAnyRole()
    - access()
- Start authorization rule
    - http.authorizeRequests() 
- Matcher method
    - .anyRequest()

### Authorization rule
- Types
    - endpoint authorization rules
        - Applied at filter chain level
    - (Global) Method Security (@RolesAllowed, @Pre/@Post Authorize, @Pre/@Post Filter)
        - Applied after the request is accpted using spring aspect
- Role vs authority (endpoint authorization rules)
    - Role is a badge, authority is an action 
    - When rules are based on action like create/delete/etc
        - authority is used
    - When rules are based on some badge like manager, admin
        - role is used
        - you know what roles exist in you system
        Usser can have multiple roles
      badges are group of permission 
    - hasRole("ADMIN")
    
### Test the application to test the role
- hit hello endpoint
    - http://localhost:8080/hello
- basic auth with john | 12345
    - works as john is admin
- basic auth with bill | 12345
    - works as bill is manager

### GrantedAuthority interface
- represents both authority and role
- Check UserDetails class. Has no separate role/authority. Has GrantedAuthority
- authorities("ROLE_ADMIN") = hasRole("ADMIN")
    - hasRole accepts roles and authorities
    - hasAuthorities accepts only authorities
- role("MANAGER") != hasAuthority("MANAGER")
- ROLE_ should be prefixed to the role
    - this is mandatory
    - spring security adds it automatically
    - Check roles methods in User class
        - if you try to add it yourself, the application wont start
        - Object of GrantedAuthority created with role
        - ROLE_ signifies that the GrantedAuthority is a badge and not an action
            - It is good practice
    
### Test the application : authority can be prefixed with ROLE_
- U1: make authorities: "ROLE_ADMIN" // a role admin using authority
- U2: make roles("MANAGER") // -> role manager
- hasRole("ADMIN")
- test it with hasRole(admin)
    - the behaviour is same as role
    - but this is not a good practice
    

### Create new endpoint for matcher method demo
- NOTE:
    - ENDPOINT = PATH + HTTP METHOD
        - GET /hello
        - POST /hello
        - GET /ciao
- controller => HelloController
- @GetMapping("/ciao")
- @PostMapping("/hola")

### Matcher methods
- tells which authorization rule to be applied to request
- Can be done for HTTP METHOD (GET/POST/etc) or PATH (/hello) or combination of both
- config=>ProjectConfig=>configure(http)
- Use case:
    - /hell0 => ADMIN
    - /ciao => MANAGER
    - /hola => Just be authenticated
- 3 types of matcher methods
    - MVC
    - ANT
    - regex
- MVC
    - before anyRequest()
    - .mvcMatchers("/hello").hasRole("ADMIN")
    - .mvcMatchers("/ciao").hasRole("MANAGER")
    - .anyRequest().isAuthenticated() //allow if authenticated //.permitAll( skips authentication) // denyAll() denys request
    
### Disable csrf for testing POST request
- config => ProjectConfig
- configure(http)
- http.csrf().disable()

### Test MVC matchers
- /hello with john (ADMIN) => works
- /hello with bill (MANAGER) => 403 Forbidden
- /ciao with bill (MANAGER) => works
- /ciao with john (ADMIN) => 403 Forbidden
- /hola POST with john (ADMIN) => works
- /hola POST with bill (MANAGER) => works

### Debugging spring security apps
- application.properties => logging.level.root=debug // should be as fine grained as possible in terms of package