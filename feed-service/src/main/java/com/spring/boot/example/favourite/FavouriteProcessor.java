package com.spring.boot.example.favourite;

import com.spring.boot.example.favourite.event.FavouriteEvent;
import com.spring.boot.example.favourite.model.Favourite;
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
@EnableBinding(FavouriteSink.class)
public class FavouriteProcessor {

    private final FavouriteRepository favouriteRepository;

    @StreamListener(value = FavouriteSink.INPUT)
    public void apply(Message<FavouriteEvent> favouriteEventMessage) {
        FavouriteEvent favouriteEvent = favouriteEventMessage.getPayload();

        log.trace("Event received: {}", favouriteEvent);

        switch (favouriteEvent.getEventType()) {
            case ADDED -> addFavourite(favouriteEvent);
            case REMOVED -> deleteFavourite(favouriteEvent);
        }
    }

    private void addFavourite(FavouriteEvent event) {
        favouriteRepository.findById(event.getId())
                .switchIfEmpty(
                        just(new Favourite()
                                .setId(event.getId()))
                )
                .flatMap(favourite -> {
                    favourite.setArticleId(event.getMessage().getArticleId())
                            .setUserId(event.getMessage().getUserId());

                    return favouriteRepository.save(favourite);
                })
                .doOnSuccess(favourite -> log.trace("Favourite with id: {} saved.", favourite.getId()))
                .doOnError(ex -> log.error("Favourite with id: {} not saved.", event.getId(), ex))
                .subscribe();
    }

    private void deleteFavourite(FavouriteEvent event) {
        favouriteRepository.findById(event.getId())
                .flatMap(favouriteRepository::delete)
                .doOnSuccess(favourite -> log.trace("Favourite with id: {} deleted.", event.getId()))
                .doOnError(ex -> log.error("Favourite with id: {} not deleted.", event.getId(), ex))
                .subscribe();
    }

}
