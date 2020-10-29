package com.spring.boot.example.user;

import com.nimbusds.jwt.JWTClaimsSet;
import com.spring.boot.example.security.SafeJwtClaimSet;
import com.spring.boot.example.user.mapper.UserMapper;
import com.spring.boot.example.user.model.User;
import com.spring.boot.example.user.model.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.function.Function;

import static reactor.core.publisher.Mono.just;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public Mono<UserDto> extractAndSaveUser(JwtAuthenticationToken authentication) {
        JWTClaimsSet.Builder jwtClaimsSetBuilder = new JWTClaimsSet.Builder();

        authentication.getToken()
                .getClaims()
                .forEach(jwtClaimsSetBuilder::claim);

        SafeJwtClaimSet claimsSet = new SafeJwtClaimSet(jwtClaimsSetBuilder.build());

        return saveUser(claimsSet, userMapper.map(claimsSet))
                .flatMap(user -> just(userMapper.map(user)));
    }

    private Mono<User> saveUser(SafeJwtClaimSet claimsSet, User user) {
        return userRepository.findByUid(user.getUid())
                .switchIfEmpty(userRepository.save(user))
                .flatMap(existingUser -> {
                    Optional<Date> updatedAt = claimsSet.getUpdatedAt();

                    if (updatedAt.isPresent()) {
                        Instant modifiedDate = updatedAt.get().toInstant();

                        if (modifiedDate.isAfter(existingUser.getLastModifiedDate())) {
                            return updateUser(user).apply(existingUser);
                        }
                    } else {
                        return updateUser(user).apply(existingUser);
                    }

                    return just(existingUser);
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
