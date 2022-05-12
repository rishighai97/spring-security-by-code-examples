# Chapter 30

#### @Secured and @RolesAllowed

## Demo: @Secured and @RolesAllowed (Copy code from chapter_29)


### Enable @SecuredEnabled and @RolesAloowed
- config => ProjectConfig
- @EnableGlobalMethodSecurity
    - add securedEnabled=true, // @Secured 
    - add jsr250Enabled = true // @RolesAllowed
- Roles allowed needs roles and not authorities
    - uds
    - authorities => roles
    - john = MANAGER
    - bill = ADMIN

### Add Secured in service
- service => DocumentService
- These annotation tell which roles are allowed
- @Secured("ROLE_MANAGER")
- Test the application
    - http://localhost:8080/documents/<username>
    - john | 12345 (MANAGER)
        - 200 OK
    - bill | 12345 (ADMIN)
        - 403 FORBIDDEN


### Add RolesAllowed in service
- service => DocumentService
- @RolesAllowed("ROLE_MANAGER") // accepts multiple values
- Test the application
  - http://localhost:8080/documents/<username>
  - john | 12345 (MANAGER)
    - 200 OK
  - bill | 12345 (ADMIN)
    - 403 FORBIDDEN
- Works same as @Secured

### Note on Secured and RolesAllowed
- Are authorization rules only on roles

## Demo: Authorization and resource server for implementing global method security authorization rules

### Get the authorization and resource server from lesson 19
- Have 2 users in auth server
  - john: authority read
  - bill: authority write
  
### Generate access token via authentication server
- Generate JWT token
  - http://localhost:8080/oauth/token?grant_type=password&username=john&password=bill&scope=read
  - basic auth client1 | secret1
  - VIew the JWT token on jwt.io
  
### Add authorization rule in resource server
- Add pre authorize annotation for read
  - controller => HelloController
  - @PreAuthorize("hasAuthority('read')") // can be done on service/repository
- Enable global method security
  - config => ResourceServerConfig
  - @EnableGlobalMethodSecurity(prePostEnabled=true)
  
### Test authorization rule on resource server
- Test for john
  - Get access token via jogn
  - hit resource server with access token
    - http://localhost:9090/hello
      - Authorization
        - Bearer | <access-token>
  - John has read authority hence we were able to access
- Test for bill
  - 403 FORBIDDEN as bill has authority write

