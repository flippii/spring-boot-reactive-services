package com.spring.boot.example.profile;

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
public class ProfileConfiguration {

    private final ProfileApiHandler profileApiHandler;

    @Bean
    public RouterFunction<?> profileRoutes() {
        RouterFunction<ServerResponse> json = route()
                .nest(accept(APPLICATION_JSON), builder -> builder
                        .GET("/{id}", profileApiHandler::profile)
                        .POST("", profileApiHandler::create)
                        .PUT("/{id}", profileApiHandler::update)
                        .DELETE("/{id}", profileApiHandler::delete)
                )
                .build();

        return route().path("/api/profiles", () -> json)
                .build();
    }

}
