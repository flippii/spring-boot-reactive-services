package com.spring.boot.example.article;

import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

public class ArticleDuplicateKeyException extends ResponseStatusException {

    public ArticleDuplicateKeyException(String reason, Throwable throwable) {
        super(UNPROCESSABLE_ENTITY, reason, throwable);
    }

}
