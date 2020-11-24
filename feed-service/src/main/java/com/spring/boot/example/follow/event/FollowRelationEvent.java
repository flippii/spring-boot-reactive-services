package com.spring.boot.example.follow.event;

import com.spring.boot.example.core.domain.DomainEvent;
import com.spring.boot.example.core.domain.EventType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class FollowRelationEvent implements DomainEvent<String> {

    private final String id;
    private final EventType eventType;
    private final Message message;

    @Getter
    @ToString
    @RequiredArgsConstructor
    public static class Message {

        private final String userId;
        private final String followId;

    }

}
