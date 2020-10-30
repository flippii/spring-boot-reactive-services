package com.spring.boot.example.keycloak;

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
@EnableConfigurationProperties(KeycloakProperties.class)
public class KeycloakConfiguration {

    private final KeycloakApiHandler keycloakApiHandler;

    @Bean
    public RouterFunction<?> keycloakRoutes() {
        RouterFunction<ServerResponse> json = route()
                .nest(accept(APPLICATION_JSON), builder -> builder
                        .GET("/", keycloakApiHandler::users)
                        .GET("/count", keycloakApiHandler::userCount))
                .build();

        return route().path("/api/keycloak/users", () -> json)
                .build();
    }

}
