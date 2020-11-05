package com.spring.boot.example.feed;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class FeedApiHandler {

    public Mono<ServerResponse> feed(ServerRequest request) {
        return Mono.empty();
    }

}
