package com.spring.boot.example.core.springDoc;

import io.swagger.v3.oas.models.OpenAPI;

@FunctionalInterface
public interface SpringDocCustomizer {

    void customize(OpenAPI openAPI);

}
