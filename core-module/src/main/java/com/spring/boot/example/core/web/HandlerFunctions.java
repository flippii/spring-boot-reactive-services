package com.spring.boot.example.core.web;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.function.Function;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HandlerFunctions {

    public static <T> Mono<ServerResponse> writeResponse(Publisher<T> publisher, Function<T, URI> created) {
        return Mono
                .from(publisher)
                .flatMap(comment ->
                        ServerResponse
                                .created(created.apply(comment))
                                .contentType(APPLICATION_JSON)
                                .build()
                );
    }

    public static  <T> Mono<ServerResponse> readResponse(Publisher<T> publisher, Class<T> clazz) {
        return ok()
                .contentType(APPLICATION_JSON)
                .body(publisher, clazz);
    }

}
