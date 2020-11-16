package com.spring.boot.example.follow;

import com.spring.boot.example.follow.model.FollowRelation;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface FollowRepository extends ReactiveMongoRepository<FollowRelation, String> {

    Flux<FollowRelation> findAllByUserId(String userId);

}
