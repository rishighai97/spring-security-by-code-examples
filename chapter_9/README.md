# Chapter 9

#### CSRF: Cross-Site Request Forgery Vulnerability

- For frontend calling a backend server, where frontend is loaded from the backend itself
- Client request server > Server generates csrf token > Client uses csrf while doing mutating REST operations

## Demo

### Create new project with dependencies

- spring web, spring security, thymeleaf(for front end)

### Create controller

- Create controller package
- Create MainController
- @Controller
- Create main() @GetMapping returning view main.html as a String

### main.html

- Crete main.html in resources>templates
- Do h1>Hello

### Test the app

- Use the usernmame password from console
- On startup you are automatically redirected to login page on doing localhost:8080 via browser
- Do user, password from console and you can view Hello
- Go to chrome inspector and viwe the login form
- There is a hidden csrf input field having a value (csrf token)
- Csrf token is used for the post request

### Disabling csrf token

- Create package called config
- Create @Configuration ProjectConfig
- extend WebSecurityConfigurerAdapter
- Override configure with HttpSecurity
- after calling super call, do http.csrf().disable() // Generally don't do this :)

### Test the app without csrf

- Restart and call endpoint
- Now button does not have input field csrf token
- login with user and password works as is

### Do a post action for testing vulnerability

- Do PostMapping "/change" // POST,PUT,DELETE are mutating actions in REST
- sout(":(")
- Return main.html
- Update main.html
    - Create Form with action "/change"
    - Add button type="submit" -> PRESS ME!

### Test the app: How CSRF vulnerability works

- Login with username and password
- On log in, you view the button
- On pressing the button, you can the log on console. This means attacker was able send file to you which you opened and
  were able to execute the post
- Create new html page malicious.html outside the project
    - copy form code from main.html
    - action = http://localhost:8080/change
    - Hitting the button would result in going back to Hello
    - The attacker forced me to do a post on the server as I was already logged in.
    - NOTE: Hence you need authorization rules too, to avoid users using endpoints they are not supposed to

### How spring security csrf works

- Comment http.csrf(),disable in ProjectConfig
- Spring security generates a random token that cannot be guessed in the login for button
- For Mutating REST operations, you need to provider csrf token provided by spring security to view via response body

### Test csrf by spring security and view csrf token in html file using thymeleaf

- Go to main page, you can view csrf token and are able to login

- Add csrf token in main.html so that it can be used to do post
    - in <html xmlns:th="http://thymeleaf.org"> to use thymeleaf
    - To make the form work
      do <input type="hidden" name="_csrf" th:name="${_csrf.parameterName}" th:value="${_csrf.token}">
    - variable used to store csrf paramName, token, etc by spring
- Re run the app
    - Login
    - in main form you can hit press me to view the details
    - Custom form has no way to know the token
- In other html page, token is absent, so you are unable to access post (Forbidden 403) O/P: Log not seen in console.
  Post was not called.
- NOTE: Why csrf works in almost all cases
    - Hacker uses man in the middle to get the csrf token somehow. Then they have to convince the user to open malicious
      page. But if you use https then it adds security
    - The token is also short lived so it is really difficult to exploit the vulnerability with csrf enable

### Configuring csrf

- In Project Config
- Customizer
    - http.csrf(c-> { });
    - Disabling csrf for certain endpoints
        - c.ignoringAntMatchers("/<some-path-say-csrfdisabled>/**)
        - Allows regex as well

### Customize csrf customizer

- Default csrf workflow
    - Token is generated for a session
    - If matches then can access else 403 error
- c.csrfTokenRepository()
    - 3 methods
        - generate token
        - save token
            - can be saved in a database too
        - load token
    - Create custom csrf repository
        - in security package create CustomCsrfTokenRepository and implement CsrfTokenRepository
        - Override the 3 methods
        - CsrfToken class
            - Used by spring security to identify a token
            - Has parameterName, token, header which are the fields we used via thymeleaf in main.html
        - generateToken
            - create object fror DefaultCsrfToken("X-CSRF", "_csrf", "123456789")
    - Add custom csrf repository object to c.csrfTokenRepository() in project config configure method

### Test custom csrf token

- You can view 12345679 on csrf token value
- Comment out custom token repository after this demo

### NOTE: When to disable csrf feature

- OAuth2 architecture does not require csrf as it uses it's own tokens. We can disable csrf token for OAuth2

### NOTE: CSRF is a filter in the filter chain

- Create CsrfTokenLoggerFiler in security package
- extends OncePerRequestFilter
- override doFilterInternal
    - Create CsrfToken object as (CsrfToken) request.getAttribute("_csrf")
    - sout("Csrf Logger Filter" + token.getToken())
    - filterChain,doFilter(request, response)
- Add filter in project config
- in configure(http)
    - http.addFilterAfter(new CsrfTokenLoggerFilter(), CsrfFilter.class)

### Test Csrf Logger

- Re run the application
- call the page and do the login. You can view the token printed by the filter