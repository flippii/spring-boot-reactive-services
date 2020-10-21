package com.spring.boot.example.tags;

import com.spring.boot.example.article.ArticleRepository;
import com.spring.boot.example.article.model.Article;
import com.spring.boot.example.article.model.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class TagService {

    private final ArticleRepository articleRepository;

    public Flux<Tag> getAll() {
        return articleRepository.findAll()
                .flatMapIterable(Article::getTags)
                .distinct();
    }

}
