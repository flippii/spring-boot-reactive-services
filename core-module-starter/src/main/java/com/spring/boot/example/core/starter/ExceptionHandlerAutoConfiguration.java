package com.spring.boot.example.core.starter;

import com.spring.boot.example.core.web.error.GlobalErrorAttributes;
import com.spring.boot.example.core.web.error.GlobalErrorWebExceptionHandler;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.web.reactive.error.ErrorWebFluxAutoConfiguration;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.codec.support.DefaultServerCodecConfigurer;

@Configuration
@AutoConfigureBefore(ErrorWebFluxAutoConfiguration.class)
public class ExceptionHandlerAutoConfiguration {

    @Bean
    public ErrorAttributes errorAttributes() {
        return new GlobalErrorAttributes();
    }

    @Bean
    @Order(-2)
    public ErrorWebExceptionHandler errorWebExceptionHandler(ApplicationContext applicationContext,
                                                             ObjectProvider<ServerCodecConfigurer> serverCodecConfigurer) {

        return new GlobalErrorWebExceptionHandler(errorAttributes(), applicationContext,
                serverCodecConfigurer.getIfAvailable(DefaultServerCodecConfigurer::new));
    }

}
