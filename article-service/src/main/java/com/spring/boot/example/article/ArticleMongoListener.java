package com.spring.boot.example.article;

import com.spring.boot.example.article.event.ArticleEvent;
import com.spring.boot.example.article.model.Article;
import com.spring.boot.example.article.model.Tag;
import com.spring.boot.example.core.domain.AbstractDomainListener;
import com.spring.boot.example.core.domain.DomainEvent;
import com.spring.boot.example.core.domain.EventType;
import org.bson.Document;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.stereotype.Component;

@Component
public class ArticleMongoListener extends AbstractDomainListener<Article, String> {

    public ArticleMongoListener(Source messageBroker, ArticleProperties articleProperties) {
        super(messageBroker, articleProperties.getMessaging().getTimeout());
    }

    @Override
    protected DomainEvent<String> createAddedEvent(Article document) {
        String[] tags = document.getTags()
                .stream()
                .map(Tag::getName)
                .toArray(String[]::new);

        return ArticleEvent.builder()
                .id(document.getId())
                .eventType(EventType.ADDED)
                .message(ArticleEvent.Message
                        .builder()
                        .slug(document.getSlug())
                        .title(document.getTitle())
                        .description(document.getDescription())
                        .body(document.getBody())
                        .tags(tags)
                        .build())
                .build();
    }

    @Override
    protected DomainEvent<String> createRemoveEvent(Document document) {
        return ArticleEvent.builder()
                .id(document.getObjectId("_id").toString())
                .eventType(EventType.REMOVED)
                .build();
    }

}
