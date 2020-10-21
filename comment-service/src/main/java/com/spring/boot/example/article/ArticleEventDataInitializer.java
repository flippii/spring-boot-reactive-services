package com.spring.boot.example.article;

import com.spring.boot.example.article.model.Article;
import com.spring.boot.example.core.initializer.DataInitializer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Slf4j
@Component
@RequiredArgsConstructor
public class ArticleEventDataInitializer implements DataInitializer {

    private final ArticleRepository articleRepository;

    @Override
    public void initialize() {
        articleRepository.deleteAll()
                .thenMany(
                        Flux
                                .just(
                                        new Article("5f906b46b5951c750e31c97c", "entwickler-magazin"),
                                        new Article("5f906b46b5951c750e31c97e", "javascript-magazin"),
                                        new Article("5f906b46b5951c750e31c97b", "java-magazin"),
                                        new Article("5f906b46b5951c750e31c97d", "php-magazin")
                                )
                                .flatMap(articleRepository::save)
                )
                .subscribe();
    }

}
