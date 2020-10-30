package com.spring.boot.example.keycloak;

import com.spring.boot.example.keycloak.model.KeycloakUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.spring.boot.example.core.web.HandlerFunctions.readResponse;
import static java.lang.String.format;
import static org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId;
import static reactor.core.publisher.Mono.error;

@Component
@RequiredArgsConstructor
public class KeycloakApiHandler {

    private final WebClient webClient;
    private final KeycloakProperties keycloakProperties;

    public Mono<ServerResponse> users(ServerRequest request) {
        int first = Integer.parseInt(request.queryParam("first").orElse("1"));
        int max = Integer.parseInt(request.queryParam("max").orElse("20"));

        Flux<KeycloakUser> userFlux = defaultResponseSpec(format("/users?max=%s&first=%s", max, first))
                .bodyToFlux(KeycloakUser.class);

        return readResponse(userFlux, KeycloakUser.class);
    }

    public Mono<ServerResponse> userCount(ServerRequest request) {
        Mono<Integer> userMono = defaultResponseSpec("/users/count")
                .bodyToMono(Integer.class);

        return readResponse(userMono, Integer.class);
    }

    private WebClient.ResponseSpec defaultResponseSpec(String uri) {
        return webClient.get()
                .uri(keycloakProperties.getUri() + uri)
                .attributes(clientRegistrationId("manager"))
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, this::handleError)
                .onStatus(HttpStatus::is5xxServerError, this::handleError);
    }

    private Mono<Throwable> handleError(ClientResponse response) {
        return error(new KeycloakClientException(response.statusCode()));
    }

}
