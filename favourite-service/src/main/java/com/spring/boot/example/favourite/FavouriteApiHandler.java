package com.spring.boot.example.favourite;

import com.spring.boot.example.article.ArticleRepository;
import com.spring.boot.example.article.model.Article;
import com.spring.boot.example.core.web.error.DocumentNotFoundException;
import com.spring.boot.example.favourite.model.Favourite;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.spring.boot.example.core.web.HandlerFunctions.readResponse;
import static com.spring.boot.example.security.ReactiveSecurityContextUtils.currentUserId;
import static reactor.core.publisher.Mono.error;
import static reactor.core.publisher.Mono.just;

@Component
@RequiredArgsConstructor
public class FavouriteApiHandler {

    private final FavouriteService favouriteService;
    private final ArticleRepository articleRepository;

    @NonNull
    public Mono<ServerResponse> favouriteArticle(ServerRequest request) {
        String slug = request.pathVariable("slug");

        Mono<Article> articleMono = articleRepository.findBySlug(slug)
                .switchIfEmpty(handleNotFoundError(slug))
                .zipWith(currentUserId())
                .flatMap(tuple ->
                        favouriteService.favourite(new Favourite(tuple.getT1().getId(), tuple.getT2()))
                                .then(just(tuple.getT1()))
                );

        return readResponse(articleMono, Article.class);
    }

    @NonNull
    public Mono<ServerResponse> unFavouriteArticle(ServerRequest request) {
        String slug = request.pathVariable("slug");

        Mono<Article> profileMono = articleRepository.findBySlug(slug)
                .switchIfEmpty(handleNotFoundError(slug))
                .zipWith(currentUserId())
                .flatMap(tuple ->
                        favouriteService.unFavourite(tuple.getT1().getId(), tuple.getT2())
                                .then(just(tuple.getT1()))
                );

        return readResponse(profileMono, Article.class);
    }

    private Mono<Article> handleNotFoundError(String slug) {
        return error(new DocumentNotFoundException("Article with slug: " + slug + " not found."));
    }

}
