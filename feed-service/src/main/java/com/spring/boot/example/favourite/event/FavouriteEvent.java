package com.spring.boot.example.favourite.event;

import com.spring.boot.example.core.domain.DomainEvent;
import com.spring.boot.example.core.domain.EventType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class FavouriteEvent implements DomainEvent<String> {

    private final String id;
    private final EventType eventType;
    private final Message message;

    @Getter
    @ToString
    @RequiredArgsConstructor
    public static class Message {

        private final String articleId;
        private final String userId;

    }

}
