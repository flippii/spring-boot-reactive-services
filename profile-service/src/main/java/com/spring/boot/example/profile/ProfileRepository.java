package com.spring.boot.example.profile;

import com.spring.boot.example.profile.model.Profile;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface ProfileRepository extends ReactiveMongoRepository<Profile, String> {

    Mono<Profile> findByUid(String uid);

    Mono<Profile> findByIdAndUid(String id, String uid);

}
