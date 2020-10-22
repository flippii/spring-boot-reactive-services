package com.spring.boot.example.infrastructure;

import com.spring.boot.example.user.UserRepository;
import com.spring.boot.example.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Locale;

import static reactor.core.publisher.Mono.error;
import static reactor.core.publisher.Mono.just;

@Slf4j
@RequiredArgsConstructor
@Component("userDetailsService")
public class DomainUserDetailsService implements ReactiveUserDetailsService {

    private final UserRepository userRepository;

    @Override
    public Mono<UserDetails> findByUsername(String login) {
        if (new EmailValidator().isValid(login, null)) {
            return userRepository.findByEmail(login)
                    .switchIfEmpty(error(new UsernameNotFoundException("User with email " + login + " was not found in the database")))
                    .flatMap(user -> just(createSpringSecurityUser(user)));
        }

        String lowercaseLogin = login.toLowerCase(Locale.ENGLISH);
        return userRepository.findByUsername(lowercaseLogin)
                .switchIfEmpty(error(new UsernameNotFoundException("User " + lowercaseLogin + " was not found in the database")))
                .flatMap(user -> just(createSpringSecurityUser(user)));
    }

    private org.springframework.security.core.userdetails.User createSpringSecurityUser(User user) {
        return new org.springframework.security.core.userdetails.User(user.getEmail(),
                user.getPassword(), Collections.emptyList());
    }

}

