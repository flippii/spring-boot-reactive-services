package com.spring.boot.example.favourite;

import com.spring.boot.example.core.domain.AbstractDomainListener;
import com.spring.boot.example.core.domain.DomainEvent;
import com.spring.boot.example.core.domain.EventType;
import com.spring.boot.example.favourite.event.FavouriteEvent;
import com.spring.boot.example.favourite.model.Favourite;
import org.bson.Document;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.stereotype.Component;

@Component
public class FavouriteMongoListener extends AbstractDomainListener<Favourite, String> {

    public FavouriteMongoListener(Source messageBroker, FavouriteProperties favouriteProperties) {
        super(messageBroker, favouriteProperties.getMessaging().getTimeout());
    }

    @Override
    protected DomainEvent<String> createAddedEvent(Favourite document) {
        return FavouriteEvent.builder()
                .id(document.getId())
                .eventType(EventType.ADDED)
                .message(FavouriteEvent.Message
                        .builder()
                        .articleId(document.getArticleId())
                        .userId(document.getUserId())
                        .build())
                .build();
    }

    @Override
    protected DomainEvent<String> createRemoveEvent(Document document) {
        return FavouriteEvent.builder()
                .id(document.getObjectId("_id").toString())
                .eventType(EventType.REMOVED)
                .build();
    }

}
