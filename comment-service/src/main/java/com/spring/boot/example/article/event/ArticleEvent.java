package com.spring.boot.example.article.event;

import com.spring.boot.example.core.domain.DomainEvent;
import com.spring.boot.example.core.domain.EventType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ArticleEvent implements DomainEvent<String> {

    private final String id;
    private final EventType eventType;
    private final Message message;

    @Getter
    @RequiredArgsConstructor
    public static class Message {

        private final String slug;
        private final String title;
        private final String description;
        private final String body;

    }

}
