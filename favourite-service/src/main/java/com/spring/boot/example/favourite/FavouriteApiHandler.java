package com.spring.boot.example.favourite;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class FavouriteApiHandler {

    public Mono<ServerResponse> favouriteArticle(ServerRequest request) {
        return Mono.empty();
    }

    public Mono<ServerResponse> unFavouriteArticle(ServerRequest request) {
        return Mono.empty();
    }

}
