package com.spring.boot.example.article;

import com.spring.boot.example.article.event.ArticleEvent;
import com.spring.boot.example.article.model.Article;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

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
        articleRepository.findById(event.getId())
                .switchIfEmpty(
                        just(new Article()
                                .setId(event.getId()))
                )
                .flatMap(article -> {
                     article.setSlug(event.getMessage().getSlug())
                            .setTitle(event.getMessage().getTitle())
                            .setDescription(event.getMessage().getDescription())
                            .setBody(event.getMessage().getBody());

                    return articleRepository.save(article);
                })
                .doOnSuccess(article -> log.trace("Article with id: {} saved.", article.getId()))
                .onErrorMap(ex -> new ArticleProcessException("Article not saved.", event.getId(), ex))
                .subscribe();
    }

    private void deleteArticle(ArticleEvent event) {
        articleRepository.findById(event.getId())
                .flatMap(articleRepository::delete)
                .onErrorMap(ex -> new ArticleProcessException("Article not deleted.", event.getId(), ex))
                .subscribe();
    }

}