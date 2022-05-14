# Chapter 28

#### Preauthorization, postauthorization, prefiltering and postfiltering

## Demo

### Create new project with dependencies

- spring web, spring security

### Create demo controller

- controller package
- DemoController
- @RestController
- @Autowire demoService
- test1()
    - @GetMapping("/test1")
    - demoService.test1()

### Create demo service

- service package
- DemoService
- @Service
- String test1()
    - sout("TEST 2")
    - return "TEST1"

### Create ProjectConfig

- config package
- ProjectConfig
- @Configuration
- extend WebSecurityConfigurerAdaper
- @EnableGlobalMethodSecurity(prePostEnabled=true)
    - Enabled methods can only be used via annotations
- Add users
    - @Bean
    - userDetailsService()
    - uds = new InMemoryUserDetailsManager()
    - user1 = User.withUsername("bill").password("12345").authorities("read").build()
      user2 = User.withUsername("john").password("12345").authorities("write").build()
    - uds.createUser(user1)
    - uds.createUser(user2)
    - return uds
- Add passwordEncoder
    - @Bean
    - passwordEncoder()
    - return NoOpPasswordEncoder.getInstance()
- Add http config
    - configure(http)
    - http.httpBasic()
    - http.authorizeRequests().anyRequest().authenticated()
        - global method security overrides this endpoint level authorization
- Test the application with no global method security authorization
    - hit test1
        - http://localhost:8080/test1
        - 401 unauthorized
    - test1 | john | 12345
        - 200 OK : TEST1

### PreAuthorize

- service => DemoServuce => test1
- @PreAuthorize("hasAuthority('read')")
- Called before calling the method
- will work only for bill
- Test the application
- test1 | john | 12345
    - 403 Forbidden
- test1 | bill | 12345
    - 200 OK

### PostAuthorize

- service => DemoService
    - String test2()
        - sout("TEST 2")
        - return "TEST2"
    - Add @PostAuthorize("hasAuthority('read')")
    - Method will execute, just an exception is thrown instead of returning the value
- add endpoint
    - controller => DemoController
    - test2()
        - @GetMapping("/test2")
        - demoService.test2()
- Test the application
    - test1 | john | 12345
        - 403 Forbidden and no o/p on console
    - test2 | john | 12345
        - 403 Forbidden
        - But you can view TEST2 in console
- NOTE:
    - Can be a security concern as method call is executed just instead of returning the value, methods throws exception
- Use case for post authorize
    - <b>When you want to test the return value in authorization rule</b>
    - service => DemoService
        - @PostAuthorize("returnObject==authentication.name")
        - String test3
        - sout("TEST3")
        - // select data from a database | john => return john
        - check data with username
        - Use it when you want to check the user with db first and then authorize
    - controller => DemoController
        - test3()
            - @GetMapping("/test3")
            - demoService.test3()
    - Test the application
        - service always returns john
        - test3 | john | 12345
            - will authorize as john (auth) equals john (db)
        - test3 | bill | 12345
            - john != bill
            - 403
            - you can still set TEST3 in console

### PreFilter

- Filters values from parameter
- Create sevice
    - service => DemoService
        - @PreFilter("filterObject != 'authentication.name'")
            - can also do == to get only authenticated user name
            - filter object refers to all the values in request collection
        - String test4(List<String> list)
        - sout(list) // filtered values
        - return list
- Create endpoint
    - controller => DemoController
        - List<String> test4()
            - @GetMapping("/test4")
            - List<String> list = new ArrayList<>();
            - list.add("john")
            - list.add("bill")
            - list.add("mary")
            - return demoService.test4(list)
- @PreFilter will filter out all the values in request not equal to john
- Test the application
    - test4 | bill | 12345
        - return <john, mary>
    - test4 | john | 12345
        - return <bill, john>
    - test 4 | mary | 12345
        - 401 unauthorized
- List.of("john", "bill", "mary") would not work as it is immutable
    - @PreFilter cannot filter it as it uses same object
    - Filter works on the same collection
    - controller => DemoController
        - List<String> test5()
            - @GetMapping("/test5")
            - list = List.of("john", "bill", "mary")
            - return demoService.test4(list)
    - Test with test5 | john | 12345
        - throws exception

### PostFilter

- Filters values from return type
- service => DemoService
    - @PostFilter("filterObject != 'authentication.name'")
        - can also do !=
        - filter object refers to all the values in request collection
    - String test6()
        - List<String> list = new ArrayList<>();
        - list.add("john")
        - list.add("bill")
        - list.add("mary")
        - sout(list) // not filtered values
        - return list
- Create endpoint
    - controller => DemoController
        - List<String> test6()
            - @GetMapping("/test6")
            - return demoService.test6()
- except 2 values as authenticated user will be filtered out
- Test the application
    - test6 | john | 12345
        - bill, mary
    - test6 | bill | 12345
        - john, mary
- Bad practice with post filter
    - Instead of writing query to get the values you need, you apply post filter
        - this is less performant
        - you should get only the value that you need from db, and not used pre/post filter for filtering
        - pre/post filter should only be used for authorizing
        - try to find other ways as pre/post filters are less performant due to search in the collection