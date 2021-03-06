package com.spring.boot.example.configuration;

import com.spring.boot.example.security.ReactiveJwtAuthorityExtractor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfiguration {

    private final String issuerUri;
    private final ReactiveJwtAuthorityExtractor reactiveJwtAuthorityExtractor;

    public SecurityConfiguration(@Value("${spring.security.oauth2.client.provider.oidc.issuer-uri}") String issuerUri,
                                 ReactiveJwtAuthorityExtractor reactiveJwtAuthorityExtractor) {

        this.issuerUri = issuerUri;
        this.reactiveJwtAuthorityExtractor = reactiveJwtAuthorityExtractor;
    }

    @Bean
    public SecurityWebFilterChain configure(ServerHttpSecurity http) {
        http
                .authorizeExchange()
                    .pathMatchers("/actuator/**")
                        .permitAll()
                    .pathMatchers("/api/users")
                        .permitAll()
                    .pathMatchers("/api/**")
                        .authenticated()
                .and()
                .oauth2Client()
                .and()
                .oauth2ResourceServer()
                    .jwt()
                        .jwtDecoder(jwtDecoder())
                        .jwtAuthenticationConverter(reactiveJwtAuthConverter());

        return http.build();
    }

    @Bean
    public Converter<Jwt, Mono<AbstractAuthenticationToken>> reactiveJwtAuthConverter() {
        ReactiveJwtAuthenticationConverter jwtAuthenticationConverter = new ReactiveJwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(reactiveJwtAuthorityExtractor);
        return jwtAuthenticationConverter;
    }

    @Bean
    public ReactiveJwtDecoder jwtDecoder() {
        return ReactiveJwtDecoders.fromOidcIssuerLocation(issuerUri);
    }

}
