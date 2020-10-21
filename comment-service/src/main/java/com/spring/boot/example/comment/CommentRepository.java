package com.spring.boot.example.comment;

import com.spring.boot.example.comment.model.Comment;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface CommentRepository extends ReactiveMongoRepository<Comment, String> {

    Flux<Comment> findByArticleId(String articleId, Sort sort);

}
