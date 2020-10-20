package com.spring.boot.example.comment;

import com.spring.boot.example.comment.model.Comment;
import com.spring.boot.example.comment.model.CommentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public Flux<Comment> getAll() {
        return commentRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    public Mono<Comment> create(CommentDto commentDto) {
        return commentRepository.save(new Comment(commentDto.getBody()));
    }

    public Mono<Comment> delete(String id) {
        return commentRepository.findById(id)
                .flatMap(comment -> commentRepository.delete(comment).thenReturn(comment));
    }

}
