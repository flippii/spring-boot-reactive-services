package com.spring.boot.example.follow;

import com.spring.boot.example.follow.event.FollowRelationEvent;
import com.spring.boot.example.follow.model.FollowRelation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import static reactor.core.publisher.Mono.just;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableBinding(FollowSink.class)
public class FollowProcessor {

    private final FollowRepository followRepository;

    @StreamListener(value = FollowSink.INPUT)
    public void apply(Message<FollowRelationEvent> followEventMessage) {
        FollowRelationEvent followRelationEvent = followEventMessage.getPayload();

        log.trace("Event received: {}", followRelationEvent);

        switch (followRelationEvent.getEventType()) {
            case ADDED -> addFollowRelation(followRelationEvent);
            case REMOVED -> deleteFollowRelation(followRelationEvent);
        }
    }

    private void addFollowRelation(FollowRelationEvent event) {
        followRepository.findById(event.getId())
                .switchIfEmpty(
                        just(new FollowRelation()
                                .setId(event.getId()))
                )
                .flatMap(followRelation -> {
                    followRelation.setTargetId(event.getMessage().getTargetId())
                            .setUserId(event.getMessage().getUserId());

                    return followRepository.save(followRelation);
                })
                .doOnSuccess(followRelation -> log.trace("FollowRelation with id: {} saved.", followRelation.getId()))
                .doOnError(ex -> log.error("FollowRelation with id: {} not saved.", event.getId(), ex))
                .subscribe();
    }

    private void deleteFollowRelation(FollowRelationEvent event) {
        followRepository.findById(event.getId())
                .flatMap(followRepository::delete)
                .doOnSuccess(followRelation -> log.trace("FollowRelation with id: {} deleted.", event.getId()))
                .doOnError(ex -> log.error("FollowRelation with id: {} not deleted.", event.getId(), ex))
                .subscribe();
    }

}
