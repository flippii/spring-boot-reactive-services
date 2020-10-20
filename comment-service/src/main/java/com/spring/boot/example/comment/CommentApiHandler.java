package com.spring.boot.example.comment;

import br.com.fluentvalidator.context.ValidationResult;
import com.spring.boot.example.comment.model.Comment;
import com.spring.boot.example.comment.model.CommentDto;
import com.spring.boot.example.core.web.error.DocumentNotFoundException;
import com.spring.boot.example.core.web.error.ValidationException;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

import static com.spring.boot.example.core.web.HandlerFunctions.readResponse;
import static com.spring.boot.example.core.web.HandlerFunctions.writeResponse;
import static reactor.core.publisher.Mono.error;

@Component
@RequiredArgsConstructor
@Tag(name = "Comment-API")
public class CommentApiHandler {

    private final CommentService commentService;
    private final CommentDtoValidator commentDtoValidator;

    public Mono<ServerResponse> comments(ServerRequest request) {
        return readResponse(commentService.getAll(), Comment.class);
    }

    public Mono<ServerResponse> create(ServerRequest request) {
        Mono<Comment> commentMono = request.bodyToMono(CommentDto.class)
                .doOnNext(commentDto -> {
                    final ValidationResult result = commentDtoValidator.validate(commentDto);

                    if (!result.isValid()) {
                        throw new ValidationException("Comment not saved, invalid data send.", result.getErrors());
                    }
                })
                .flatMap(commentService::create);

        return writeResponse(commentMono, (comment) -> URI.create("/api/comments/" + comment.getId()));
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        String commentId = request.pathVariable("id");

        Mono<Comment> commentMono = commentService.delete(commentId)
                .switchIfEmpty(error(new DocumentNotFoundException("Can't delete comment with id \"" + commentId + "\".")));

        return readResponse(commentMono, Comment.class);
    }

}
