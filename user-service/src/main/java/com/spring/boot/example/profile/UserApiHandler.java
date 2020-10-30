package com.spring.boot.example.profile;

import com.spring.boot.example.profile.model.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.spring.boot.example.core.web.HandlerFunctions.readResponse;
import static reactor.core.publisher.Mono.error;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserApiHandler {

    private final UserService userService;

    public Mono<ServerResponse> user(ServerRequest request) {
        Mono<UserDto> principalMono = request.principal()
                .cast(JwtAuthenticationToken.class)
                .doOnError(ex -> error(new IllegalArgumentException("AuthenticationToken is not OAuth2 or JWT!", ex)))
                .flatMap(userService::extractAndSaveUser);

        return readResponse(principalMono, UserDto.class);
    }

    public Mono<ServerResponse> update(ServerRequest request) {
        return Mono.empty();
    }

}
