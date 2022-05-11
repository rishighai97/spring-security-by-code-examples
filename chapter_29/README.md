# Chapter 29

#### Using Permissions

- helps us to decouple more complex authorization rules implemented via Global Method Security
- NOTE:
    - @Pre/Post Authorize/Filter annoations can be used on the same method at the same time

## Demo

### Create new project with dependencies
- spring web, spring security, lombok

### Create controller
- controllers package
- DocumentController
- @RestController
- @Autowire documentService
- List<Document> findDocuments(@PathVariable String username)
    - @GetMapping("/documents/{username}")
    - return doucumentService.findDocuments(username)
    
### Create model
- model package
- Document
    - String text
    - String user
    - NoArgsConstructor, Getters, Setters

### Create service
- service package
- DocumentService
- @Service
- List<Document> findDocuments(String username)
    - return List.of()

### Create configuration
- config package
- ProjectConfig
- @Configuration
- @EnableGlobalMethodSecurity(prePostEnabled = true)
- Add users
    - UserDetailsService
    - @Bean
    - uds = new InMemoryUserDetailsManager()
    - user1 = User.withUsername("john").password("12345").authorities("read").build()
    - user2 = User.withUsername("bill").password("12345").authorities("write").build()
    - uds.createUser(user1)
    - uds.createUser(user2)
    - return uds
- Add password encoder
    - PasswordEncoder
    - @Bean
    - return NoOpPasswordEncoder.getInstance()
    
### Setup permission evaluator
- Create Permission Evaluator
    - create security package
    - DocumentPermissionEvaluator
        - takes authentication object from Security Context, you need not pass it when calling evaluator via global method security annotations
        - implement PermissionEvaluator
        - override hasPermission methods (2)
            - we use first one with 3 parameters. Return false in the method returning 4 parameters
        - hasPermission(authentication, targetObject, permission)
        - hasPermission(authentication, targetID, type ,permission)
- Plug in the permission evaluator in the ProjectConfig
    - config => ProjectConfig
    - extend GlobalMethodSecurityConfiguration
    - permissionEvaluator()
        - return new DocumentPermissionEvaluator()
    - override createExpressionHandler
        - meh = new DefaultMethodSecurityExpressionHandler()
        - meh.setPermissionEvaluator(permissionEvaluator())


### GLobal method security annotation to check if document user = authenticated user using our custom Permission Evaluator
- service => DocumentService
- @PostAuthorize("hasPermission(returnObject, 'read')") // can pass '' or null
- findDocument(String username)
    - check if document belongs to username
    - var doc = new Document()
    - doc.setUser("john")
    - doc.setTest("TEXT")
    - return List.of(doc)
    
### Test the application by adding break point in hasPermission in DocumentPermissionEvaluator (security)
- start application in debug mode
- hit http://localhost:8080/documents/john with basic auth john|12345
    - you come to the break point in has permission method
    - this proves that our setup is correct
    
### Implementing authorization rule
- security => DocumentPermissionEvaluator
- List<Document> returnedList = (List<Document>) targetObject
- String username = authentication.getName()
- String authority = (String) permission
- boolean docsBelongToTheAuthUser = returnedList.stream().allMatch(d -> d.getUser().equals(username))
- boolean hasProperAuthority = authentication.getAuthorities().stream().anyMatch(g->g.getAuthority().equals(authority))
- return docsBelongToTHeUser && hasProperAuthority

### Note
- When you want to write a complex authorization rule, you use permission evaluator to decouple the authorization logic
- Do not make you security complex to complex as they degrade the performance

### Test the application with permission evaluator implemented
- Hit /document/john with john|12345
    - 200 OK as owner = john and authority = read
- Hit /document/bill with bill|12345
    - 403 FORBIDDEN as owner != john and authority != read
    
### Note
- This example did not need username as path variable since we use username from authenticated user