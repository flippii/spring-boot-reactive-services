package com.spring.boot.example.article;

import br.com.fluentvalidator.context.ValidationResult;
import com.spring.boot.example.article.model.Article;
import com.spring.boot.example.article.model.ArticleDto;
import com.spring.boot.example.core.web.error.DocumentNotFoundException;
import com.spring.boot.example.core.web.error.ValidationException;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.error;

@Component
@RequiredArgsConstructor
public class ArticleApiHandler {

    private final ArticleService articleService;
    private final ArticleDtoValidator articleDtoValidator;

    public Mono<ServerResponse> article(ServerRequest request) {
        String slug = request.pathVariable("slug");

        Mono<Article> articleMono = articleService.getBySlug(slug)
                .switchIfEmpty(error(new DocumentNotFoundException("Article with slug \"" + slug + "\" not found.")));

        return readResponse(articleMono);
    }

    public Mono<ServerResponse> update(ServerRequest request) {
        String slug = request.pathVariable("slug");

        Mono<Article> articleMono = request.bodyToMono(ArticleDto.class)
                .doOnNext(articleDto -> {
                    final ValidationResult result = articleDtoValidator.validate(articleDto);

                    if (!result.isValid()) {
                        throw new ValidationException("Article not saved, invalid data send.", result.getErrors());
                    }
                })
                .flatMap(articleDto -> articleService.update(slug, articleDto))
                .switchIfEmpty(error(new DocumentNotFoundException("Can't update article with slug \"" + slug + "\".")));

        return readResponse(articleMono);
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        String slug = request.pathVariable("slug");

        Mono<Article> articleMono = articleService.delete(slug)
                .switchIfEmpty(error(new DocumentNotFoundException("Can't delete article with slug \"" + slug + "\".")));

        return readResponse(articleMono);
    }

    private static Mono<ServerResponse> readResponse(Publisher<Article> publisher) {
        return ok()
                .contentType(APPLICATION_JSON)
                .body(publisher, Article.class);
    }

}
