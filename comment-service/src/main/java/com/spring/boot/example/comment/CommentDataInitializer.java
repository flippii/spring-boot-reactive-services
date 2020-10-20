package com.spring.boot.example.comment;

import com.spring.boot.example.comment.model.Comment;
import com.spring.boot.example.core.initializer.DataInitializer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommentDataInitializer implements DataInitializer {

    private final CommentRepository commentRepository;

    @Override
    public void initialize() {
        commentRepository.deleteAll()
                .thenMany(
                        Flux
                                .just("Tolles Produkt.", "Super.")
                                .flatMap(body -> commentRepository.save(new Comment(body)))
                )
                .subscribe();
    }

}
