package com.spring.boot.example.user;

import com.spring.boot.example.core.initializer.DataInitializer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserDataInitializer implements DataInitializer {

    private final UserRepository userRepository;

    @Override
    public void initialize() {

    }

}
