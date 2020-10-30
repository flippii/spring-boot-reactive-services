package com.spring.boot.example.security;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import reactor.core.publisher.Mono;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ReactiveSecurityContextUtils {

    private static final String UID_CLAIM = "test";

    public static Mono<String> currentUserId() {
        return jwt().map(jwt -> jwt.getClaimAsString(UID_CLAIM));
    }

    public static Mono<Jwt> jwt() {
        return ReactiveSecurityContextHolder.getContext()
                .map(context -> context.getAuthentication().getPrincipal())
                .cast(Jwt.class);
    }

}
