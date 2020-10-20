package com.spring.boot.example.core.web.error;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.Map;

public class GlobalErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Map<String, Object> errorAttributes = super.getErrorAttributes(request, options);
        Throwable error = getError(request);

        if (error instanceof  ValidationException) {
            handleValidationException(errorAttributes, error);
        }

        return errorAttributes;
    }

    private void handleValidationException(Map<String, Object> errorAttributes, Throwable error) {
        errorAttributes.put("validation", ((ValidationException) error).getErrors());
    }

}
