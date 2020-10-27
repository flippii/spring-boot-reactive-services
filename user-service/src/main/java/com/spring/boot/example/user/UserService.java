package com.spring.boot.example.user;

import com.nimbusds.jwt.JWTClaimsSet;
import com.spring.boot.example.user.mapper.UserMapper;
import com.spring.boot.example.user.model.User;
import com.spring.boot.example.user.model.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public Mono<UserDto> extractAndSaveUser(Authentication authentication) {
        if (!(authentication instanceof JwtAuthenticationToken)) {
            throw new IllegalArgumentException("AuthenticationToken is not OAuth2 or JWT!");
        }

        JwtAuthenticationToken authToken = (JwtAuthenticationToken) authentication;

        JWTClaimsSet.Builder jwtClaimsSetBuilder = new JWTClaimsSet.Builder();

        authToken.getToken()
                .getClaims()
                .forEach(jwtClaimsSetBuilder::claim);

        JWTClaimsSet jwtClaimsSet = jwtClaimsSetBuilder.build();

        return saveUser(authToken.getTokenAttributes(), userMapper.map(jwtClaimsSet))
                .flatMap(user -> Mono.just(userMapper.map(user)));
    }

    private Mono<User> saveUser(Map<String, Object> details, User user) {
        return userRepository.findByUid(user.getUid())
                .switchIfEmpty(userRepository.save(user))
                .flatMap(existingUser -> {
                    if (details.get("updated_at") != null) {
                        Instant modifiedDate = Date.from((Instant) details.get("updated_at")).toInstant();

                        if (modifiedDate.isAfter(existingUser.getLastModifiedDate())) {
                            return updateUser(user).apply(existingUser);
                        }
                    } else {
                        return updateUser(user).apply(existingUser);
                    }

                    return Mono.just(existingUser);
                });
    }

    private Function<User, Mono<User>> updateUser(User user) {
        return (existingUser) -> {
            existingUser.setFirstName(user.getFirstName())
                    .setLastName(user.getLastName());

            if (user.getEmail() != null) {
                existingUser.setEmail(user.getEmail().toLowerCase());
            }

            existingUser.setLangKey(user.getLangKey())
                    .setImageUrl(user.getImageUrl());

            return userRepository.save(existingUser);
        };
    }

}
