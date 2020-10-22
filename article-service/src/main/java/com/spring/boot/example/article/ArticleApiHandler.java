package com.spring.boot.example.article;

import br.com.fluentvalidator.context.ValidationResult;
import com.spring.boot.example.article.model.Article;
import com.spring.boot.example.article.model.ArticleDto;
import com.spring.boot.example.core.web.error.DocumentNotFoundException;
import com.spring.boot.example.core.web.error.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.spring.boot.example.core.web.HandlerFunctions.readResponse;
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

        return readResponse(articleMono, Article.class);
    }

    public Mono<ServerResponse> create(ServerRequest request) {
        Mono<Article> articleMono = request.bodyToMono(ArticleDto.class)
                .doOnNext(this::validateArticle)
                .flatMap(articleService::create)
                .switchIfEmpty(error(new DocumentNotFoundException("Can't create new article.")));

        return readResponse(articleMono, Article.class);
    }

    public Mono<ServerResponse> update(ServerRequest request) {
        String slug = request.pathVariable("slug");

        Mono<Article> articleMono = request.bodyToMono(ArticleDto.class)
                .doOnNext(this::validateArticle)
                .flatMap(articleDto -> articleService.update(slug, articleDto))
                .switchIfEmpty(error(new DocumentNotFoundException("Can't update article with slug \"" + slug + "\".")));

        return readResponse(articleMono, Article.class);
    }

    private void validateArticle(ArticleDto articleDto) {
        final ValidationResult result = articleDtoValidator.validate(articleDto);

        if (!result.isValid()) {
            throw new ValidationException("Article not saved, invalid data send.", result.getErrors());
        }
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        String slug = request.pathVariable("slug");

        Mono<Article> articleMono = articleService.delete(slug)
                .switchIfEmpty(error(new DocumentNotFoundException("Can't delete article with slug \"" + slug + "\".")));

        return readResponse(articleMono, Article.class);
    }

}
