package com.spring.boot.example.article;

import com.spring.boot.example.article.event.ArticleEvent;
import com.spring.boot.example.article.model.Article;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import reactor.core.publisher.Mono;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableBinding(ArticleSink.class)
public class ArticleProcessor {

    private final ArticleRepository articleRepository;

    @StreamListener(value = ArticleSink.INPUT)
    public void apply(Message<ArticleEvent> articleEventMessage) {
        ArticleEvent articleEvent = articleEventMessage.getPayload();

        log.trace("");

        switch (articleEvent.getEventType()) {
            case ADDED -> addArticle(articleEvent);
            case REMOVED -> deleteArticle(articleEvent);
        }
    }

    private void addArticle(ArticleEvent event) {
        articleRepository.save(new Article(event.getId(), event.getMessage().getSlug()))
                .switchIfEmpty(Mono.error(new RuntimeException("Article not saved.")))
                .subscribe();
    }

    private void deleteArticle(ArticleEvent event) {
        articleRepository.deleteById(event.getId())
                .switchIfEmpty(Mono.error(new RuntimeException("Article not deleted.")))
                .subscribe();
    }

}
