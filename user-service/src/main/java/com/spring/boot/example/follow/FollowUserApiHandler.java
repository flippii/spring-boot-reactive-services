package com.spring.boot.example.follow;

import com.spring.boot.example.core.web.error.DocumentNotFoundException;
import com.spring.boot.example.follow.model.FollowRelation;
import com.spring.boot.example.profile.UserRepository;
import com.spring.boot.example.profile.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.spring.boot.example.core.web.HandlerFunctions.readResponse;
import static com.spring.boot.example.security.ReactiveSecurityContextUtils.currentUserId;
import static reactor.core.publisher.Mono.error;

@Component
@RequiredArgsConstructor
public class FollowUserApiHandler {

    private final FollowService followService;
    private final UserRepository userRepository;
    private final FollowRepository followRepository;

    public Mono<ServerResponse> follow(ServerRequest request) {
        String userId = request.pathVariable("userId");

        Mono<User> userMono = userRepository.findByUid(userId)
                .zipWith(currentUserId())
                .flatMap(tuple -> {
                    FollowRelation followRelation = new FollowRelation(tuple.getT2(), tuple.getT1().getUid());
                    return followService.saveRelation(followRelation)
                            .then(userRepository.findByUid(tuple.getT1().getUid()));
                })
                .switchIfEmpty(handleUserNotFoundError(userId));

        return readResponse(userMono, User.class);
    }

    public Mono<ServerResponse> unfollow(ServerRequest request) {
        String userId = request.pathVariable("userId");

        Mono<User> userMono = userRepository.findByUid(userId)
                .zipWith(currentUserId())
                .flatMap(tuple ->
                     followRepository.findByIdAndTargetId(tuple.getT2(), tuple.getT1().getId())
                            .flatMap(followRelation ->
                                    followService.removeRelation(followRelation)
                                        .then(userRepository.findByUid(tuple.getT1().getId()))
                            )
                            .switchIfEmpty(error(new DocumentNotFoundException("")))
                )
                .switchIfEmpty(handleUserNotFoundError(userId));

        return readResponse(userMono, User.class);
    }

    private Mono<User> handleUserNotFoundError(String userId) {
        return error(new DocumentNotFoundException("User with id: " + userId + " not found."));
    }

}
