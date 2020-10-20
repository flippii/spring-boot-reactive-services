package com.spring.boot.example.article;

import com.spring.boot.example.article.event.ArticleEvent;
import com.spring.boot.example.article.model.Article;
import com.spring.boot.example.core.domain.AbstractDomainListener;
import com.spring.boot.example.core.domain.EventType;
import org.bson.Document;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.stereotype.Component;

@Component
public class ArticleMongoListener extends AbstractDomainListener<Article, ArticleEvent> {

    public ArticleMongoListener(Source messageBroker, ArticleProperties articleProperties) {
        super(messageBroker, articleProperties.getMessaging().getTimeout());
    }

    @Override
    protected ArticleEvent createAddedEvent(Article document) {
        return ArticleEvent.of(document.getId(), EventType.ADDED)
                .setMessage(document.getSlug(), document.getTitle(), document.getDescription(), document.getBody());
    }

    @Override
    protected ArticleEvent createRemoveEvent(Document document) {
        return ArticleEvent.of(document.getObjectId("_id").toString(), EventType.REMOVED);
    }

}
