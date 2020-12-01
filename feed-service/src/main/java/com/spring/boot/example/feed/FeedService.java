package com.spring.boot.example.feed;

import com.spring.boot.example.feed.model.FeedData;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import static org.springframework.data.domain.Sort.Direction.ASC;

@Service
@RequiredArgsConstructor
public class FeedService {

    private final ReactiveMongoTemplate mongoTemplate;

    public Flux<FeedData> getUserFeed(String uid) {
        LookupOperation lookup = LookupOperation.newLookup()
                .from("article-favourite")
                .localField("followId")
                .foreignField("userId")
                .as("favourite");

        ProjectionOperation project = Aggregation.project("userId", "followId")
                .andInclude(Fields.from(
                        Fields.field("articleId", "$favourite.articleId")
                ));

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(new Criteria("userId").is(uid)),
                lookup,
                Aggregation.unwind("favourite", true),
                project,
                Aggregation.match(new Criteria("articleId").ne(null)),
                Aggregation.sort(ASC, "articleId")
        );

        return mongoTemplate.aggregate(aggregation, "user-follow", FeedData.class);
    }

}
