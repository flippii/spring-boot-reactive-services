package com.spring.boot.example.article;

import lombok.Getter;

@Getter
public class ArticleProcessException extends RuntimeException {

    private final String articleId;

    public ArticleProcessException(String message, String articleId, Throwable throwable) {
        super(message, throwable);
        this.articleId = articleId;
    }

}
