package com.spring.boot.example.security;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimAccessor;
import reactor.core.publisher.Mono;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ReactiveSecurityContextUtils {

    public static Mono<String> currentUserId() {
        return jwt()
                .map(JwtClaimAccessor::getSubject);
    }

    public static Mono<Jwt> jwt() {
        return ReactiveSecurityContextHolder.getContext()
                .map(context -> context.getAuthentication().getPrincipal())
                .cast(Jwt.class);
    }

}
