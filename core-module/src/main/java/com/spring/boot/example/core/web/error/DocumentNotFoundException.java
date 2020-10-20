package com.spring.boot.example.core.web.error;

import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

public class DocumentNotFoundException extends ResponseStatusException {

    public DocumentNotFoundException(String reason) {
        super(NOT_FOUND, reason);
    }

}
