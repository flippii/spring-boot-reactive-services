package com.spring.boot.example.comment;

import com.spring.boot.example.comment.model.CommentDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.bind.annotation.RequestMethod.*;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RequestPredicates.contentType;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
public class CommentConfiguration {

    private final CommentApiHandler commentApiHandler;

    @Bean
    @RouterOperations({
            @RouterOperation(path = "/api/articles/{slug}/comments", beanClass = CommentApiHandler.class, beanMethod = "comments", method = GET,
                    operation = @Operation(operationId = "comments",
                            parameters = { @Parameter(in = ParameterIn.PATH, name = "slug", description = "Article Slug") },
                            responses = { @ApiResponse(content = { @Content(schema = @Schema(anyOf = CommentDto.class))})
                    }
            )),
            @RouterOperation(path = "/api/articles/{slug}/comments", beanClass = CommentApiHandler.class, beanMethod = "create", method = POST,
                   operation = @Operation(operationId = "create",
                           parameters = { @Parameter(in = ParameterIn.PATH, name = "slug", description = "Article Slug") },
                           requestBody = @RequestBody(required = true, content = { @Content(schema = @Schema(implementation = CommentDto.class)) }),
                           responses = { @ApiResponse(content = { @Content(schema = @Schema()) })
                   }
            )),
            @RouterOperation(path = "/api/articles/{slug}/comments/{id}", beanClass = CommentApiHandler.class, beanMethod = "delete", method = DELETE,
                   operation = @Operation(operationId = "delete",
                           parameters = {
                                    @Parameter(in = ParameterIn.PATH, name = "slug", description = "Article Slug"),
                                    @Parameter(in = ParameterIn.PATH, name = "id", description = "Comment Id")
                           },
                           responses = { @ApiResponse(content = { @Content(schema = @Schema(implementation = CommentDto.class)) })
                   }
            ))
    })
    public RouterFunction<?> articleRoutes() {
        RouterFunction<ServerResponse> json = route()
                .nest(accept(APPLICATION_JSON), builder -> builder
                        .GET("/", commentApiHandler::comments)
                        .POST("/", contentType(APPLICATION_JSON), commentApiHandler::create)
                        .DELETE("/{id}", commentApiHandler::delete))
                .build();

        return route().path("/api/articles/{slug}/comments", () -> json)
                .build();
    }

    @Bean
    public GroupedOpenApi commentsOpenApi() {
        return GroupedOpenApi.builder()
                .group("comments")
                .pathsToMatch(new String[] { "/api/articles/**" })
                .build();
    }

}
