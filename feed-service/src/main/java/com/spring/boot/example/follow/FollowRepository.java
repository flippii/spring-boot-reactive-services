package com.spring.boot.example.follow;

import com.spring.boot.example.follow.model.FollowRelation;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface FollowRepository extends ReactiveMongoRepository<FollowRelation, String> {
}
