package com.chapter.thirty.one.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class DemoController {

    @GetMapping("/demo")
    @PreAuthorize("hasAuthority('write')")
    public Mono<String> demo() { // demo(Mono<Authentication> monoAuthentication)
//        return Mono.just("Demo!");
        Mono<Authentication> monoAuthentication = ReactiveSecurityContextHolder
                .getContext()
                .map(SecurityContext::getAuthentication);
        return monoAuthentication.map(ma->"Hello, ".concat(ma.getName()));
    }

}
