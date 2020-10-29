package com.spring.boot.example.follow;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class FollowUserApiHandler {

    public Mono<ServerResponse> follow(ServerRequest request) {

    }

    public Mono<ServerResponse> unfollow(ServerRequest request) {

    }

}
