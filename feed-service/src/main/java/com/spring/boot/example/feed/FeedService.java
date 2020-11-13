package com.spring.boot.example.feed;

import com.spring.boot.example.feed.model.FeedData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class FeedService {

    //TODO: Profil daten per web client
    public Flux<FeedData> getUserFeed(String uid) {
        return Flux.empty();
    }

}
