package com.spring.boot.example.feed;

import com.spring.boot.example.feed.model.FeedData;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.spring.boot.example.core.web.HandlerFunctions.readResponse;
import static com.spring.boot.example.security.ReactiveSecurityContextUtils.currentUserId;

@Component
@RequiredArgsConstructor
public class FeedApiHandler {

    private final FeedService feedServie;

    @NonNull
    public Mono<ServerResponse> feed(ServerRequest request) {
        Flux<FeedData> feedDataFlux = currentUserId()
                .flatMapMany(feedServie::getUserFeed);

        return readResponse(feedDataFlux, FeedData.class);
    }

}
