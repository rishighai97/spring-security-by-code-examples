# Chapter 14

#### Using Opaque tokens: Introspection

- After resource server gets the access token, it needs to validate the token with authorization server
- Need for validation comes as opaque token do not provide any information about the user

## Demo Auth server
- NOTE: Refer chapter_14_1 for Resource server

### Create new project with dependencies 
- spring web, spring security, oauth2

### Setup authorization server
- Create configuration
    - Config package
    - Create UserManagementConfig
        - @Configuration
        - Copy UserManagementConfig from previous module
    - Create AuthServerConfig
        - @Configuration @EnableAuthorizationServer 
        - extend AuthorizationServerConfigurerAdapter
        - Pick configurations from previous module
    - NOTE: For auth server, resource server is a client
    - Add a client for resource server
        - in configure (clients)
            - and().withClient("resourceserver").secret("12345")
            - no need of scopes and grant types as we only call check token endpoint
- Test auth server via postman client
    -  get access token
        - http://localhost:8080/oauth/token?grant_type=password&username=bill&password=12345&scope=read
    - check if resource server would be able to call check token endpoint
        - http:localhost:8080/oauth/check_token?token=asddsd GET
        - <b> Note: never share credentials between clients </b>
    

## Demo: Resource server (chapter_14_1)

### Create new project with dependencies
- spring web, spring security, oauth2

### Create ednpoint to test 
- Create package controller
- Create HelloController with @RestController
- Create @GetMapping "/hello" returning a string "Hello!"

### Create Resource server configurations
- Create package config
    - Create class ResourceServerConfig
    - Do @Configuration and @EnableResourceServer
- create application.properties and do a server.port=9090

### Test the application
- Without token introspection
    - do localhost:8080/hello
- With token introspection
    - 1: get token
        - http://localhost:8080/oauth/token?grant_type=password&username=bill&password=12345&scope=read
    - 2: hit /hello with token
        - Add header : Authorization | Bearer <token>
        - hit localhost:9090/hello
- Both cases do not work as resource server and auth server do not have access to the same token store now
- Case 2 works if we provider client:secret with basic auth.
- So we will have to configure auth server with client secret in resource server to make it work

### Setup token introspection
- We provide resource server the endpoint to introspect the token
    - we can do this by adding following property in app,properties
        - security.oauth2.resource,token-info-uri:http:localhost:8080/oauth/check_token // this automatically creates token store with same contract as auth server
- As we have done isAuthenticated in auth server, we need to provide client credentials in resource server
    - add following properties to application.properties
        - security.oauth2.client.client-id=resourceserver
        - security.oauth2.client.client-secret=12345

### Test the resource server
- Rerun the application
- Generate token
    - http://localhost:8080/oauth/token?grant_type=password&username=bill&password=12345&scope=read
- Call /hello with token
    - Add header : Authorization | Bearer <token> // authorization does not work without sending the header
    - hit localhost:9090/hello
- Now it works
        
    