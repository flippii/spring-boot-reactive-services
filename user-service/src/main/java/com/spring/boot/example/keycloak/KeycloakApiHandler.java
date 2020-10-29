package com.spring.boot.example.keycloak;

import com.spring.boot.example.keycloak.model.KeycloakUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.spring.boot.example.core.web.HandlerFunctions.readResponse;
import static org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId;

@Component
@RequiredArgsConstructor
public class KeycloakApiHandler {

    private final WebClient webClient;

    public Mono<ServerResponse> users(ServerRequest request) {
        int first = Integer.parseInt(request.queryParam("first").orElse("1"));
        int max = Integer.parseInt(request.queryParam("max").orElse("20"));

        Flux<KeycloakUser> userFlux = webClient.get()
                .uri("http://localhost:9090/auth/admin/realms/jhipster/users?max=" + max + "&first=" + first)
                .attributes(clientRegistrationId("manager"))
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> {
                    System.out.println("4xx error");
                    return Mono.error(new RuntimeException("4xx"));
                })
                .onStatus(HttpStatus::is5xxServerError, response -> {
                    System.out.println("5xx error");
                    return Mono.error(new RuntimeException("5xx"));
                })
                .bodyToFlux(KeycloakUser.class);

        return readResponse(userFlux, KeycloakUser.class);
    }

    public Mono<ServerResponse> userCount(ServerRequest request) {
        Mono<Integer> userMono = webClient.get()
                .uri("http://localhost:9090/auth/admin/realms/jhipster/users/count")
                .attributes(clientRegistrationId("manager"))
                .retrieve()
                .bodyToMono(Integer.class);

        return readResponse(userMono, Integer.class);
    }

}
