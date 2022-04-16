# Chapter 6

#### Two-factor Authentication Part 1: Username and password and otp authentication to generate a token

- 2 step authentication
    - Username and password
    - OTP which generates a token to access endpoint

## Demo

### Create new project with spring web, security and data jpa, mysql connector

### Make connection to database
- Add following to application.properties
    - spring.datasource.url=jdbc:mysql://localhost/ss_chapter5
    - spring.datasource.username=root
    - spring.datasource.password=rishighai
- Create user table
    - id int PK | NN | AI
    - username varchar
    - password varchar
- Create otp table
    - Make id PK | NN | AI
    - username varchar
    - otp varchar
    
### create packages
- controller, config, entity, repository

### Create entities
- User
    - @Entity
    - int id (@Id, @GeeneratedValue(GenerationType.IDENTITY))
    - String username and password
    - Getter and Setter
- Otp
    - @Entity
    - int id (@Id, @GeeneratedValue(GenerationType.IDENTITY))
    - String username, otp
    
### Create repositoties
- UserRepository
    - @Repository
    - extend JpaRepository<User, Integer>
- OtpRepository
    - @Repositoru
    - extends JpaRepository<Otp, Integer>

### Create controller
- Hello Controller with @GetMapping returning a String ("/login")

### Create configuration
- ProjectConfig
    - @Configuration
    - extend WebSecurityConfigurerAdapter
    - Override
    
### Create UserDetailsService
- Create JpaUserDetailsService
    - @Service
    - implement UserDetailsService
    - override loadUserByUsername
    - Autowire UserRepository
    - Implement Optional<User> findUserByUsername(String username) in user repository
    - Call userRepository.findUserbyUsername(username)
    - Do user.orElseThrow(UsernameNotFoundException)
    - <b>Create SecurityUser wrapping User for making it of type UserDetails in models package</b>
        - Create package security and create class SecurityUser
        - Implement UserDetails and add field User user
        - Override all the methods  and create a constructor as well
        - In authorities return read, return true wherever applicable. Return username and password from the user object
    - Return SecurityUser object
    
### Add password encoder
- In project config, create a bean returning a NoOp

### Creating custom filter and custom provider packages (filter, packages)

### Creating a custom filter
- Create UsernamePasswordAuthFilter
    - @Component
    - extend OncePerRequestFilter
    - Override doFilterInternal and shouldNotFilter
    - should not filter
        - Return !request.getServletPath().equals("/login") : Filter should not work for any endpoint other than login
    - Configure AuthenticationManager
        - Autowire AuthenticationManager bean
        - Create authenication manager bean in Project Config by overriding authenticationManagerBean
    - Implement doFilterInternal
        - Step1: username and password
        - Step2: username and otp
        - get username by request.getHeader("username")
        - get password by request.getHeader("password")
        - get otp by request.getHeader("otp")f
        - if (otp==null) step 1
        - else step 2
        - for two authentication implementation, we need two authentication objects
            - Ceate package called authentications in security
            - UsernamePasswordAuthentication step 1
                - extend UsernamePasswordAuthentication
                - Create both the constructors
            - OtpAuthentication step 2
                - extend UsernamePasswordAuthentication
                - Create both the constructors
        - Step 1:
            - create object Authentication a for UsernamePasswordAuthentication(username , password)
            - authenticationManager.authenticate(a)
            - SecurityContextHolder.getContext().setAuthentication(a)
        - Step 2:
            - create object a for OtpAuthentication(username , otp)
            - authenticationManager.authenticate(a)
            - SecurityContextHolder.getContext().setAuthentication(a)
    
### Creating custom authentication providers
- UsernamePasswordAuthProvider
    - @Component
    - implements AuthenticationProvider
    - Overrides authenticate and supports
    - supports
        - UsernamePasswordAuthentication.class.equals(authType)
    - Autowire JpaUserDetailsService as userDetailsService and PasswordEncoder beans
    - autheticate(authentication)
        - username = authentication.getName()
        - password = authentication.getCredentials() as a String
        - UserDetails user = userDetailsService.loadUserByUsername()
        - if passwordEncoder matches password and db password, return new Object for UsernamePasswordAuthentication(username, password, user.getAuthorities())
        - Note Constructor with 3 parameters is necessary as it sets isAuthenticated to true
        - else throw BadCredentialsException
- OtpAuthenticationProvider
    - Same as above, just replace  
        - password with otp
        - supports checks for OtpAuthentication.class
    - authenticate(Authetnication)
        - get username and otp from authentication
        - Autowire OtpRepository after implementing otpRepository.findOtpByUsername(username)
        - o = otpRepository,finfOtpByUsername(username)
        - if o.isPresent and o.get().getPassword().equals(otp), return new OtpAuthentication(username, otp , read authorities)
        - else throw BadCredentialsException()
    

### Generating otp in UsernamePasswordAuthFilter
- Inject OtpRepository
- In Step 1, generate otp
    - code = String.valueOf(new Random().nextInt(9999) + 1000)
    - Create otpEntity object and set it's username and otp(code)
    - do otpRepository.save(otpEntity)
- In Step 2, generate token
    - response.setHeader("Authorization", UUID.randomUUID().toString())

### Setup filter and providers
- Override configure methdods  with authManagerBuilder and http
- authBuilder (providers)
    - set authenticationpProviders
    - Autowire UsernamePasswordAuthProvider and OtpAuthenticationProvider
    - auth.authenticationProvider(authProvider).authenticationProvider(otpAuthenticationProvider)
- http (filters)
    - Autowire UsernamePasswordAuthFilter
    - http.addFilterAt(usernamePasswordAuthFilter , BasicAuthenticationFilter)
    
### Add user 
- 1, bill, 12345

### Test 

- step1:
    - http://localhost:8080/login
    - username = bill, password = 12345
    - db otp table should have an otp
- step 2:
    - username = bill, otp = 10108
- o/p
    - uuid