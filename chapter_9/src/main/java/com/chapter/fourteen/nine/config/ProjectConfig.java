package com.chapter.fourteen.nine.config;

import com.chapter.fourteen.nine.security.csrf.CustomCsrfTokenRepository;
import com.chapter.fourteen.nine.security.filter.CsrfTokenLoggerFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CsrfFilter;

@Configuration
public class ProjectConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
//        http.csrf().disable(); // Generally never disable it
        http.csrf(c -> {
            c.ignoringAntMatchers("/csrfdisabled/**"); // will disable csrf for endpoint /csrfdisabled/test
            c.csrfTokenRepository(new CustomCsrfTokenRepository());
        });
        http.addFilterAfter(new CsrfTokenLoggerFilter(), CsrfFilter.class);
    }

}
