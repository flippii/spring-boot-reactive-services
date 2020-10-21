package com.spring.boot.example.comment;

import com.spring.boot.example.article.ArticleRepository;
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
    private final ArticleRepository articleRepository;

    public Flux<Comment> getBySlug(String slug) {
        return articleRepository.findBySlug(slug)
                .flatMapMany(article ->
                        commentRepository.findByArticleId(article.getId(), Sort.by(Sort.Direction.DESC, "id"))
                );
    }

    public Mono<Comment> create(String slug, CommentDto commentDto) {
        return articleRepository.findBySlug(slug)
                .flatMap(article ->
                        commentRepository.save(new Comment(commentDto.getBody(), article.getId()))
                );
    }

    public Mono<Comment> delete(String slug, String id) {
        return articleRepository.findBySlug(slug)
                .flatMap(article ->
                        commentRepository.findById(id)
                                .flatMap(comment -> commentRepository.delete(comment).thenReturn(comment))
                );
    }

}
