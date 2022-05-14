# Chapter 14

#### Validating opaque tokens via Token Introspection

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
    - get access token
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
- create application.properties and do a server.port=9090 erties to application.properties -
  security.oauth2.client.client-id=resourceserver - security.oauth2.client.client-secret=12345

### Test the resource server

- Rerun the application
- Generate token
    - http://localhost:8080/oauth/token?grant_type=password&username=bill&password=12345&scope=read
- Call /hello with token
    - Add header : Authorization | Bearer <token> // authorization does not work without sending the header
    - hit localhost:9090/hello
- Now it works
        
    