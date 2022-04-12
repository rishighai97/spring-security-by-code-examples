package com.chapter.two.service;

import com.chapter.two.domain.SpringSecurityUser;
import com.chapter.two.entity.User;
import com.chapter.two.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public class JPAUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findOneByUsername(username);
        User user = userOptional.orElseThrow(()->new UsernameNotFoundException("Username record not found in database"));
        return new SpringSecurityUser(user);
    }

}
