package com.spring.boot.example.article;

import com.spring.boot.example.article.model.Article;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface ArticleRepository extends ReactiveMongoRepository<Article, String> {

    Mono<Article> findByArticleId(String articleId);

    Mono<Article> findBySlug(String slug);

}
