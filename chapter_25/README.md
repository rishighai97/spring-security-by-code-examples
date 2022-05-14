# Chapter 25

#### MVC matchers and ANT matchers

## Demo

### Create new project with dependencies

- spring web and spring security

### Create endpoint to authorize

- controllers package
- DemoController
- @RestController
- @GetMapping("/a") returns "a"
- @GetMapping("/a/b") returns "ab"
- @GetMapping("/a/b/c") returns "abc"
- @GetMapping("/b") returns "b"
- @GetMapping("/b/c") returns "bc"
- @GetMapping("/c/{name}") @PathVariable name
    - return "Hello" + name

### Create configuration

- config package
- ProjectConfig
- @Configuration
- UserDetailsService
    - @Bean
    - uds = new InMemoryUserDetailsManager()
    - userDetails1 = User.withUsername("bill").password("12345").authority("read").build()
    - userDetails2 = User.withUsername("john").password("12345").authority("write").build()
    - uds.createUser(userDetails1)
    - uds.createUser(userDetails2)
    - return uds
- PasswordEncoder
    - @Bean
    - NoOpPasswordEncoder.getInstance()

### Setup authorization rules

- config => ProjectConfig
- extend WebSecurityConfigurerAdapter
- override configure(http)
- http.basic()
- http.authorizeRequests()
- Matchers
    - mvcMatcher()
    - antMatcher()
    - --> ANT path expressions

### MVC Matcher

- config => ProjectConfig => configure(http)
- Scenario 1:Exact match
    - .mvcMatchers("/a").hasAuthority("read")
    - .anyRequest().denyAll()
        - everything above (bill) is applied else is rejected
    - Test the application via postman
        - /a | bill | 12345
            - a
        - /b | bill | 12345
            - 403 Forbidden
        - /a/b | bill | 12345
            - 403
- Scenario 2 : /** 0 or more path name
    - .mvcMatchers("/a", "/a/b", "/a/b/c", ..... and many more with parameters)
    - <b>Use Ant query expression language to solve this scenario</b>
        - /** => 0 or more path names
        - .mvcMatchers("/a/**").hasAuthority("read")
            - works for any request starting with /a
            - from pur example, only /b is not covered
        - .anyRequest().denyAll()
    - Test the application
        - /a/b/c | bill | 12345
        - /a , /a/b are also accessible with bill
        - /b with bill returns 403 Forbidden
    - /a/**/b (0 or more path names between /a and /b)
        - /a/b | /a/b/b | /a/b/c/b/c/b
- Scenario 3: /* only one path name
    - /* => 0
        - /b/*
            - /b/c => works
            - /b or /b/c/d => does not work
        - /b
            - /b works
            - /b/c does not work
    - .mvcMatchers("/b").hasAuthority("write")
    - .mvcMatchers("/b/*).authenticated()
        - any authenticated user can call
    - .mvcMatchers("/a/**").hasAuthority("read")
    - .anyRequest().denyAll()
    - Test the application
        - /a | /a/b | /a/b/c works with bill
        - /b works with john
        - /b/c works with any authenticated user
- ** and * can be used prefix infix and suffix in the path
- ** and * can be used multiple times and together in the path
- Scenario 4: parameter name in path
    - .mvcMatchers("/b").hasAuthority("write")
    - .mvcMatchers("/b/*).authenticated()
    - .mvcMatchers("/a/**").hasAuthority("read")
    - .mvcMatchers("/c/{name}).authenticated()
        - any authenticated user can call
    - Test the application
        - /c/john with john
        - Hello john
- Scenario 5 parameter with regex
    - /c/{name: regex}
        - when the paramter variable matches the regex

### Ant Matcher

- Scenario 1 /**:
    - http.authorizeRequests()
        - .antMatchers("/a/**").hasAuthority("read")
        - .anyRequest().denyAll()
    - /a | /a/b | /a/b/c all work with bill
    - works same as mvc matcher
- Scenario 2 (mvc vs ant matchers) /a works but /a/ bypasses authentication (causes vulnerability in your application)
    - http.authorizeRequests()
    - .antMatchers("/a").authenticated()
    - .anyRequest().permitAll()
    - Test the application:
        - /a with bill => works
        - /a without authentication => returns 401 unauthorized
        - <b>/a/ without credentials works!!</b>
    - Reason: /a goes to antMatcher and /a/ goes to permitAll()
        - you will have to explicitly do /a and /a/ in antMatcher
        - or use mvcMatcher()
            - mvc matcher works in the way springs understands the paths (spring handler mapping).
            - ant matcher does not work inline with spring handler mapping
        - Test the application
            - /a and /a/ works now only when valid credentials match and do not work without valid credentials
- NOTE: Prefer mvcMatcher vs antMatcher
- Scenario 3: Adding http method
    - .mvcMatchers(HttpMethod.GET, "/a").authenticated()

### Regex Matcher

- Avoided as they are very complex