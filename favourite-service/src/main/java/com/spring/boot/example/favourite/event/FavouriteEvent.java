package com.spring.boot.example.favourite.event;

import com.spring.boot.example.core.domain.DomainEvent;
import com.spring.boot.example.core.domain.EventType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FavouriteEvent implements DomainEvent<String> {

    private final String id;
    private final EventType eventType;
    private final Message message;

    @Getter
    @Builder
    public static class Message {

        private final String userId;

    }

}
