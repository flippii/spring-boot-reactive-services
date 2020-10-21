package com.spring.boot.example.tags;

import com.spring.boot.example.article.model.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.spring.boot.example.core.web.HandlerFunctions.readResponse;

@Component
@RequiredArgsConstructor
public class TagApiHandler {

    private final TagService tagService;

    public Mono<ServerResponse> tags(ServerRequest request) {
        return readResponse(tagService.getAll(), Tag.class);
    }

}
