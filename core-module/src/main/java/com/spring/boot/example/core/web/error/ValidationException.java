package com.spring.boot.example.core.web.error;

import br.com.fluentvalidator.context.Error;
import lombok.Getter;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;

import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@Getter
public class ValidationException extends ResponseStatusException {

    private final Collection<Error> errors;

    public ValidationException(String reason, Collection<Error> errors) {
        super(UNPROCESSABLE_ENTITY, reason);
        this.errors = errors;
    }

}
