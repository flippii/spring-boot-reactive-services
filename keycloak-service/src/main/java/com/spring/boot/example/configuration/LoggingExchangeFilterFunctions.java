package com.spring.boot.example.configuration;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
class LoggingExchangeFilterFunctions {

    static ExchangeFilterFunction logRequestFilterFunction() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            log.info("Request: {} {}", clientRequest.method(), clientRequest.url());

            log.info("--- Http Headers: ---");
            clientRequest.headers()
                    .forEach(LoggingExchangeFilterFunctions::logHeader);

            log.info("--- Http Cookies: ---");
            clientRequest.cookies()
                    .forEach(LoggingExchangeFilterFunctions::logHeader);

            return Mono.just(clientRequest);
        });
    }

    static ExchangeFilterFunction logResponseFilterFunction() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            log.info("Response: {}", clientResponse.statusCode());
            clientResponse.headers().asHttpHeaders()
                    .forEach(LoggingExchangeFilterFunctions::logHeader);
            return Mono.just(clientResponse);
        });
    }

    private static void logHeader(String name, List<String> values) {
        values.forEach(value -> log.info("{}={}", name, value));
    }

}
