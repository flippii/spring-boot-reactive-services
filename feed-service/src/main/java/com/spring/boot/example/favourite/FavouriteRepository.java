package com.spring.boot.example.favourite;

import com.spring.boot.example.favourite.model.Favourite;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface FavouriteRepository extends ReactiveMongoRepository<Favourite, String> {
}
