package com.spring.boot.example.user;

import com.spring.boot.example.user.model.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.spring.boot.example.core.web.HandlerFunctions.readResponse;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserApiHandler {

    private final UserService userService;

    public Mono<ServerResponse> account(ServerRequest request) {
        Mono<UserDto> principalMono = ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .flatMap(userService::extractAndSaveUser);

        return readResponse(principalMono, UserDto.class);
    }

}
