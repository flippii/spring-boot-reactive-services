package com.spring.boot.example.favourite;

import com.spring.boot.example.core.web.error.DocumentNotFoundException;
import com.spring.boot.example.favourite.model.Favourite;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static reactor.core.publisher.Mono.error;
import static reactor.core.publisher.Mono.just;

@Service
@RequiredArgsConstructor
public class FavouriteService {

    private final FavouriteRepository favouriteRepository;

    public Mono<Favourite> favourite(Favourite favourite) {
        return favouriteRepository.findByArticleIdAndUserId(favourite.getArticleId(), favourite.getUserId())
                .switchIfEmpty(favouriteRepository.save(favourite));
    }

    public Mono<Favourite> unFavourite(String articleId, String uid) {
        return favouriteRepository.findByArticleIdAndUserId(articleId, uid)
                .switchIfEmpty(error(new DocumentNotFoundException("Favourite with articleId: " + articleId + " not found.")))
                .flatMap(favourite ->
                        favouriteRepository.delete(favourite).then(just(favourite))
                );
    }

}
