package com.spring.boot.example.article.event;

import com.spring.boot.example.core.domain.DomainEvent;
import com.spring.boot.example.core.domain.EventType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ArticleEvent implements DomainEvent<String> {

    private final String id;
    private final EventType eventType;
    private Message message;

    public static ArticleEvent of(String id, EventType eventType) {
        return new ArticleEvent(id, eventType);
    }

    public ArticleEvent setMessage(String slug, String title, String description, String body) {
        this.message = new Message(slug, title, description, body);
        return this;
    }

    @Getter
    @RequiredArgsConstructor
    public static class Message {

        private final String slug;
        private final String title;
        private final String description;
        private final String body;

    }

}
