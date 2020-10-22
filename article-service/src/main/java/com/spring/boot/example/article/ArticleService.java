package com.spring.boot.example.article;

import com.spring.boot.example.article.model.Article;
import com.spring.boot.example.article.model.ArticleDto;
import com.spring.boot.example.article.model.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;

    public Mono<Article> getBySlug(String slug) {
        return articleRepository.findBySlug(slug);
    }

    public Mono<Article> create(ArticleDto articleDto) {
        Article article = new Article(
                articleDto.getTitle(),
                articleDto.getDescription(),
                articleDto.getBody(),
                transformTags(articleDto.getTags()));

        return articleRepository.save(article);
    }

    public Mono<Article> update(String slug, ArticleDto articleDto) {
        return articleRepository.findBySlug(slug)
                .flatMap(article -> {
                    article.setTitle(articleDto.getTitle())
                        .setDescription(articleDto.getDescription())
                        .setBody(articleDto.getBody())
                        .setTags(transformTags(articleDto.getTags()));

                    return articleRepository.save(article);
                });
    }

    private Set<Tag> transformTags(String[] tags) {
        return Arrays.stream(tags)
                .map(Tag::new)
                .collect(toSet());
    }

    public Mono<Article> delete(String slug) {
        return articleRepository.findBySlug(slug)
                .flatMap(article -> articleRepository.delete(article).thenReturn(article));
    }

}
