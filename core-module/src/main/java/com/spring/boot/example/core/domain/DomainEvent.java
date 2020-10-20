package com.spring.boot.example.core.domain;

public interface DomainEvent<T> {

    T getId();

    EventType getEventType();

}
