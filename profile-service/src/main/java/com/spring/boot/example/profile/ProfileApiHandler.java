package com.spring.boot.example.profile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProfileApiHandler {

    private final ProfileService profileService;

    public Mono<ServerResponse> profile(ServerRequest request) {
        return Mono.empty();
    }

    public Mono<ServerResponse> create(ServerRequest request) {
        return Mono.empty();
    }

    public Mono<ServerResponse> update(ServerRequest request) {
        return Mono.empty();
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        return Mono.empty();
    }

}
