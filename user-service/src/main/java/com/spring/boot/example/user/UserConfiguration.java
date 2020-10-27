package com.spring.boot.example.user;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
public class UserConfiguration {

    private final UserApiHandler userApiHandler;

    @Bean
    public RouterFunction<?> articleRoutes() {
        RouterFunction<ServerResponse> json = route()
                .nest(accept(APPLICATION_JSON), builder -> builder
                        .GET("/account", userApiHandler::account))
                .build();

        return route().path("/api", () -> json)
                .build();
    }

}
