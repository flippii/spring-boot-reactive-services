package com.spring.boot.example.feed;

import com.spring.boot.example.feed.model.FeedData;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class FeedService {

    private final ReactiveMongoTemplate mongoTemplate;

    public Flux<FeedData> getUserFeed(String uid) {
        MatchOperation matchStage = Aggregation.match(new Criteria("userId").is("1"));
        ProjectionOperation projectStage = Aggregation.project("userId", "followId");

        Aggregation aggregation = Aggregation.newAggregation(matchStage, projectStage);

        return mongoTemplate.aggregate(aggregation, "user-follow", FeedData.class);
    }

}
