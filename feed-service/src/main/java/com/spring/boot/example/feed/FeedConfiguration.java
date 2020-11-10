package com.spring.boot.example.feed;

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
public class FeedConfiguration {

    private final FeedApiHandler feedApiHandler;

    @Bean
    public RouterFunction<?> followRoutes() {
        RouterFunction<ServerResponse> json = route()
                .nest(accept(APPLICATION_JSON), builder -> builder
                        .GET("", feedApiHandler::feed))
                .build();

        return route().path("/api/feed", () -> json)
                .build();
    }

}