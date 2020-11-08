package com.spring.boot.example.favourite;

import com.spring.boot.example.favourite.model.Favourite;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface FavouriteRepository extends ReactiveMongoRepository<Favourite, String> {

    Mono<Favourite> findByIdAndUserId(String articleId, String userId);

}
