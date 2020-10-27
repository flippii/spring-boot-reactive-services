package com.spring.boot.example.user;

import com.nimbusds.jwt.JWTClaimsSet;
import com.spring.boot.example.user.mapper.UserMapper;
import com.spring.boot.example.user.model.User;
import com.spring.boot.example.user.model.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.text.ParseException;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.function.Function;

import static java.util.Optional.ofNullable;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public Mono<UserDto> extractAndSaveUser(Authentication authentication) {
        if (!(authentication instanceof JwtAuthenticationToken)) {
            throw new IllegalArgumentException("AuthenticationToken is not OAuth2 or JWT!");
        }

        JWTClaimsSet.Builder jwtClaimsSetBuilder = new JWTClaimsSet.Builder();

        ((JwtAuthenticationToken) authentication).getToken()
                .getClaims()
                .forEach(jwtClaimsSetBuilder::claim);

        JWTClaimsSet claimsSet = jwtClaimsSetBuilder.build();

        return saveUser(claimsSet, userMapper.map(claimsSet))
                .flatMap(user -> Mono.just(userMapper.map(user)));
    }

    @SneakyThrows(value = ParseException.class)
    private Mono<User> saveUser(JWTClaimsSet claimsSet, User user) {
        return userRepository.findByUid(user.getUid())
                .switchIfEmpty(userRepository.save(user))
                .flatMap(existingUser -> {
                    Optional<Date> updatedAt = ofNullable(claimsSet.getDateClaim("updated_at"));

                    if (updatedAt.isPresent()) {
                        Instant modifiedDate = updatedAt.get().toInstant();

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
