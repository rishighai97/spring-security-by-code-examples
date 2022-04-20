# Chapter 8

#### Security Context
- Security Context is the place where spring security stores the authenticated user
- Spring security uses security context to get user for authorization
- Security context can be accessed from anywhere in the application

## Demo

### Get Authentication from security context in controller
- Add Authentication object field in hello endpoint method
- Print and Return Hello+authentication.getName()

## Test above step
- Generate token and hit hello endpoint with token
- You can view the token object

### Get authentication from security context in any place other than the controller method field
- In controller, authentication = SecurityContextHolder.getContext().getAuthentication()
- Restart and test

### Problems when not running one thread per request
- Enable async
    - In project config add @EnableAsync
    - Make controller method @Async
- Restart and test
- You get an exception that authentication from security context is null
- Reason:
    - Spring app is one request per thread when doing async
    - Spring creates separate thread for authentication and request
    - SecurityContextHolder default strategy = MODE_THREADLOCAL (single thread)

### Security Context Holder
- Has 3 modes
- Default is single threaded
- Object managing security context

### MODE_INHERITABLETHREADLOCAL
- Copies security context from parent to child thread
- Go to ProjectConfig
- Create InitializingBean (called when initializing a context)
    - return () -> SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL)
    - Can also be used via spring.security.strategy system property
- Test by restarting the application
    - Generate token and hit hello endpoint with the token
    - Now you can view the token in the console

### Alternative to Inheritable thread local
- <b>NOTE: works only when spring has created and managed by spring</b>
    - Test by disabling async
    - Create runnable with existing hello code
    - Create Executors.newSingleThreadedExecutor()
    - service.submit(runnable)
    - service.shutdown
    - Test the application
        - You can view hello. (THis issue was there in older spring version. Works fine now)
- Option 1: Create DelegatingSecurityContextRunnable
    - Remove Initializing bean (Re running would print null)
    - In the hello method in controller, create DelegatingSecurityContextRunnable object to wrap your runnable
    - pass delegatingSecurityContextRunnble object to service.submit
    - Test by regenerating token and trying to access hello endpoint
- Option 2: Create DelegatingContextExecutorService object by wrapping the service
    - Test by regenerating token and trying to access hello endpoint
    
### Note: Try to rely on spring for thread execution wherever you can in an enterprise application