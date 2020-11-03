package com.spring.boot.example.profile;

import com.spring.boot.example.core.initializer.DataInitializer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProfileDataInitializer implements DataInitializer {

    private final ProfileRepository profileRepository;

    @Override
    public void initialize() {
        profileRepository.deleteAll().subscribe();
    }

}
