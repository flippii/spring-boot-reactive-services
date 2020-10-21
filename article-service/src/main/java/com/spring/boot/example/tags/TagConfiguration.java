package com.spring.boot.example.tags;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
public class TagConfiguration {

    private final TagApiHandler tagApiHandler;

    @Bean
    public RouterFunction<?> tagRoutes() {
        return route(GET("/api/tags"), tagApiHandler::tags);
    }

}
