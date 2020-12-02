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
        LookupOperation favouriteLookup = LookupOperation.newLookup()
                .from("article-favourite")
                .localField("followId")
                .foreignField("userId")
                .as("favourite");

        ProjectionOperation project = Aggregation.project("userId", "followId")
                .andInclude(Fields.from(
                        Fields.field("articleId", "$favourite.articleId")
                ));

        LookupOperation articleLookup = LookupOperation.newLookup()
                .from("articles")
                .localField("articleId")
                .foreignField("articleId")
                .as("article");

        AddFieldsOperation addFields = Aggregation.addFields()
                .addFieldWithValueOf("articleSize", ArrayOperators.arrayOf("article").length())
                .build();

        LookupOperation articleFavouriteLookup = LookupOperation.newLookup()
                .from("article-favourite")
                .localField("articleId")
                .foreignField("articleId")
                .as("articleFavourite");

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(new Criteria("userId").is(uid)),
                favouriteLookup,
                Aggregation.unwind("favourite", true),
                project,
                Aggregation.match(new Criteria("articleId").ne(null)),
                articleLookup,
                addFields,
                Aggregation.match(new Criteria("articleSize").gte(1)),
                Aggregation.unwind("article", true),
                articleFavouriteLookup,
                Aggregation.sort(ASC, "articleId")
        );

        return mongoTemplate.aggregate(aggregation, "user-follow", FeedData.class);
    }

}
