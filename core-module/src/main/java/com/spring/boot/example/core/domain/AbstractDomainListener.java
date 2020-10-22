package com.spring.boot.example.core.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterDeleteEvent;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.messaging.support.MessageBuilder;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractDomainListener<T, E> extends AbstractMongoEventListener<T> {

    private final Source messageBroker;
    private final long timeOut;

    @Override
    public final void onAfterSave(AfterSaveEvent<T> event) {
        DomainEvent<E> domainEvent = createAddedEvent(event.getSource());

        log.trace("Send domain event: {}", domainEvent);

        sendDomainEvent(domainEvent);
    }

    protected abstract DomainEvent<E> createAddedEvent(T document);

    @Override
    public final void onAfterDelete(AfterDeleteEvent<T> event) {
        DomainEvent<E> domainEvent = createRemoveEvent(event.getSource());

        log.trace("Send domain event: {}", domainEvent);

        sendDomainEvent(domainEvent);
    }

    protected abstract DomainEvent<E> createRemoveEvent(Document document);

    private void sendDomainEvent(DomainEvent<E> event) {
        messageBroker.output()
                .send(MessageBuilder.withPayload(event).build(), timeOut);
    }

}
