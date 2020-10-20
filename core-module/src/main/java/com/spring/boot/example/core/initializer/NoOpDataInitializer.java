package com.spring.boot.example.core.initializer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NoOpDataInitializer implements DataInitializer {

    @Override
    public void initialize() {
        log.trace("No beans to import data registered in context.");
    }

}
