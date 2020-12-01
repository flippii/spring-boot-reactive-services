package com.spring.boot.example.feed;

import com.spring.boot.example.feed.model.FeedData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@ExtendWith(SpringExtension.class)
@Import({FeedService.class, FeedDataInitializer.class})
public class FeedServiceTest {

    private static final AtomicInteger count = new AtomicInteger(0);

    @Autowired
    private FeedDataInitializer feedDataInitializer;

    @Autowired
    private FeedService feedService;

    @BeforeEach
    void before() {
        feedDataInitializer.initialize();
    }

    @Test
    void testUserFeed() {
        Flux<FeedData> feedDataFlux = feedService.getUserFeed("1");

        //feedDataFlux.doOnNext(System.out::println)
        //        .subscribe();

        StepVerifier
                .create(feedDataFlux)
                .assertNext(this::assertFeedData)
                .assertNext(this::assertFeedData)
                .assertNext(this::assertFeedData)
                .assertNext(this::assertFeedData)
                .assertNext(this::assertFeedData)
                .expectComplete()
                .verify();
    }

    private void assertFeedData(FeedData feedData) {
        assertThat(feedData)
                .isNotNull()
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(feedDataStub().get(count.getAndIncrement()));
    }

    private List<FeedData> feedDataStub() {
        return List.of(
                new FeedData()
                        .setUserId("1")
                        .setFollowId("2")
                        .setArticleId("1"),
                new FeedData()
                        .setUserId("1")
                        .setFollowId("2")
                        .setArticleId("2"),
                new FeedData()
                        .setUserId("1")
                        .setFollowId("2")
                        .setArticleId("3"),
                new FeedData()
                        .setUserId("1")
                        .setFollowId("2")
                        .setArticleId("4"),
                new FeedData()
                        .setUserId("1")
                        .setFollowId("2")
                        .setArticleId("5")
        );
    }

}
