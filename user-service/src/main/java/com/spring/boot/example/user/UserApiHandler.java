package com.spring.boot.example.user;

import com.spring.boot.example.user.model.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.spring.boot.example.core.web.HandlerFunctions.readResponse;
import static org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserApiHandler {

    private final UserService userService;
    private final WebClient webClient;

    public Mono<ServerResponse> account(ServerRequest request) {
        Mono<UserDto> principalMono = ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .flatMap(userService::extractAndSaveUser);

        return readResponse(principalMono, UserDto.class);
    }

    public Mono<ServerResponse> users(ServerRequest request) {
        Flux<String> userFlux = webClient.get()
                .uri("http://localhost:9090/auth/admin/realms/jhipster/users")
                .attributes(clientRegistrationId("manager"))
                .retrieve()
                .bodyToFlux(String.class);

        return readResponse(userFlux, String.class);
    }

}
