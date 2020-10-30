package com.spring.boot.example.keycloak;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class KeycloakClientException extends ResponseStatusException {

    public KeycloakClientException(HttpStatus status) {
        super(status);
    }

}
