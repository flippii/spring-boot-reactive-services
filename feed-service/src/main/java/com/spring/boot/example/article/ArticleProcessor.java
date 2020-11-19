package com.spring.boot.example.article;

import com.spring.boot.example.article.event.ArticleEvent;
import com.spring.boot.example.article.model.Article;
import com.spring.boot.example.article.model.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import java.util.Arrays;
import java.util.stream.Collectors;

import static reactor.core.publisher.Mono.just;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableBinding(ArticleSink.class)
public class ArticleProcessor {

    private final ArticleRepository articleRepository;

    @StreamListener(value = ArticleSink.INPUT)
    public void apply(Message<ArticleEvent> articleEventMessage) {
        ArticleEvent articleEvent = articleEventMessage.getPayload();

        log.trace("Event received: {}", articleEvent);

        switch (articleEvent.getEventType()) {
            case ADDED -> addArticle(articleEvent);
            case REMOVED -> deleteArticle(articleEvent);
        }
    }

    private void addArticle(ArticleEvent event) {
        articleRepository.findByArticleId(event.getId())
                .switchIfEmpty(
                        just(new Article()
                                .setArticleId(event.getId()))
                )
                .flatMap(article -> {
                    article.setSlug(event.getMessage().getSlug())
                            .setTitle(event.getMessage().getTitle())
                            .setDescription(event.getMessage().getDescription())
                            .setBody(event.getMessage().getBody())
                            .setTags(Arrays.stream(event.getMessage().getTags())
                                    .map(Tag::new)
                                    .collect(Collectors.toSet()));

                    return articleRepository.save(article);
                })
                .doOnSuccess(article -> log.trace("Article with id: {} saved.", article.getId()))
                .doOnError(ex -> log.error("Article with id: {} not saved.", event.getId(), ex))
                .subscribe();
    }

    private void deleteArticle(ArticleEvent event) {
        articleRepository.findByArticleId(event.getId())
                .flatMap(articleRepository::delete)
                .doOnSuccess(article -> log.trace("Article with id: {} deleted.", event.getId()))
                .doOnError(ex -> log.error("Article with id: {} not deleted.", event.getId(), ex))
                .subscribe();
    }

}
