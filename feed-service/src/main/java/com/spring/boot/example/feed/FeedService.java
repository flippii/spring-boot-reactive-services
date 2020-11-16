package com.spring.boot.example.feed;

import com.spring.boot.example.article.ArticleRepository;
import com.spring.boot.example.feed.model.FeedData;
import com.spring.boot.example.follow.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import static reactor.core.publisher.Mono.just;

@Service
@RequiredArgsConstructor
public class FeedService {

    private final FollowRepository followRepository;
    private final ArticleRepository articleRepository;

    //TODO: artikel beinhaltet eine Liste aller user/favourite auflösen? feed über aggregation?
    //TODO: Profil daten per web client
    public Flux<FeedData> getUserFeed(String uid) {
        return followRepository.findAllByUserId(uid)
                .flatMap(followRelation -> {


                    return just(new FeedData());
                });
    }

}
