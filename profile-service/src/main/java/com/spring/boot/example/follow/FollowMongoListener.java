package com.spring.boot.example.follow;

import com.spring.boot.example.core.domain.AbstractDomainListener;
import com.spring.boot.example.core.domain.DomainEvent;
import com.spring.boot.example.core.domain.EventType;
import com.spring.boot.example.follow.event.FollowRelationEvent;
import com.spring.boot.example.follow.model.FollowRelation;
import org.bson.Document;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.stereotype.Component;

@Component
public class FollowMongoListener extends AbstractDomainListener<FollowRelation, String> {

    public FollowMongoListener(Source messageBroker, FollowProperties followProperties) {
        super(messageBroker, followProperties.getMessaging().getTimeout());
    }

    @Override
    protected DomainEvent<String> createAddedEvent(FollowRelation document) {
        return FollowRelationEvent.builder()
                .id(document.getId())
                .eventType(EventType.ADDED)
                .message(FollowRelationEvent.Message
                        .builder()
                        .userId(document.getUserId())
                        .targetId(document.getTargetId())
                        .build())
                .build();
    }

    @Override
    protected DomainEvent<String> createRemoveEvent(Document document) {
        return FollowRelationEvent.builder()
                .id(document.getObjectId("_id").toString())
                .eventType(EventType.REMOVED)
                .build();
    }

}
