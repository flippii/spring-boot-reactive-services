package com.spring.boot.example.follow;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface FollowSink {

    String INPUT = "follow";

    @Input(FollowSink.INPUT)
    SubscribableChannel follow();

}
