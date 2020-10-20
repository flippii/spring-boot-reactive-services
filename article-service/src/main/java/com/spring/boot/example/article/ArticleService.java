package com.spring.boot.example.article;

import com.spring.boot.example.article.model.Article;
import com.spring.boot.example.article.model.ArticleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;

    public Mono<Article> getBySlug(String slug) {
        return articleRepository.findBySlug(slug);
    }

    public Mono<Article> update(String slug, ArticleDto articleDto) {
        return articleRepository.findBySlug(slug)
                .flatMap(article -> {
                    article.setTitle(articleDto.getTitle());
                    article.setDescription(articleDto.getDescription());
                    article.setBody(articleDto.getBody());
                    return articleRepository.save(article);
                });
    }

    public Mono<Article> delete(String slug) {
        return articleRepository.findBySlug(slug)
                .flatMap(article -> articleRepository.delete(article).thenReturn(article));
    }

}
