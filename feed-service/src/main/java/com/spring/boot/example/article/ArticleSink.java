package com.spring.boot.example.article;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface ArticleSink {

    String INPUT = "article";

    @Input(ArticleSink.INPUT)
    SubscribableChannel article();

}
