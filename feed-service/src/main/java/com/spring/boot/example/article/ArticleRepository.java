package com.spring.boot.example.article;

import com.spring.boot.example.article.model.Article;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ArticleRepository extends ReactiveMongoRepository<Article, String> {

    //TODO: Query mi "in"
    //https://docs.mongodb.com/manual/reference/operator/query/in/

}
