package com.spring.boot.example.favourite;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface FavouriteSink {

    String INPUT = "favourite";

    @Input(FavouriteSink.INPUT)
    SubscribableChannel favourite();

}
