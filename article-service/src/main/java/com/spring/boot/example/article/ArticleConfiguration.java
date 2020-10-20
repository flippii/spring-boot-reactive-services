package com.spring.boot.example.article;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(ArticleProperties.class)
public class ArticleConfiguration {

    private final ArticleApiHandler articleApiHandler;

    @Bean
    public RouterFunction<?> articleRoutes() {
        RouterFunction<ServerResponse> json = route()
                .nest(accept(APPLICATION_JSON), builder -> builder
                        .GET("/{slug}", articleApiHandler::article)
                        .PUT("/{slug}", contentType(APPLICATION_JSON), articleApiHandler::update)
                        .DELETE("/{slug}", articleApiHandler::delete))
                .build();

        return route().path("/api/articles", () -> json)
                .build();
    }

}
