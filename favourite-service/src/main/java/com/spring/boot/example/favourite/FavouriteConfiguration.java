package com.spring.boot.example.favourite;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(FavouriteProperties.class)
public class FavouriteConfiguration {

    private final FavouriteApiHandler favouriteApiHandler;

    @Bean
    public RouterFunction<?> followRoutes() {
        RouterFunction<ServerResponse> json = route()
                .nest(accept(APPLICATION_JSON), builder -> builder
                        .POST("", favouriteApiHandler::favouriteArticle)
                        .DELETE("", favouriteApiHandler::unFavouriteArticle))
                .build();

        return route().path("/api/articles/{slug}/favourite", () -> json)
                .build();
    }

}
