package com.spring.boot.example.follow;

import com.spring.boot.example.core.web.error.DocumentNotFoundException;
import com.spring.boot.example.follow.model.FollowRelation;
import com.spring.boot.example.profile.ProfileRepository;
import com.spring.boot.example.profile.model.Profile;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.spring.boot.example.core.web.HandlerFunctions.readResponse;
import static com.spring.boot.example.security.ReactiveSecurityContextUtils.currentUserId;
import static reactor.core.publisher.Mono.error;

@Component
@RequiredArgsConstructor
public class FollowApiHandler {

    private final FollowService followService;
    private final ProfileRepository profileRepository;
    private final FollowRepository followRepository;

    @NonNull
    public Mono<ServerResponse> follow(ServerRequest request) {
        String userId = request.pathVariable("userId");

        Mono<Profile> userMono = profileRepository.findByUid(userId)
                .zipWith(currentUserId())
                .flatMap(tuple ->
                        followService.saveRelation(new FollowRelation(tuple.getT2(), tuple.getT1().getUid()))
                                .then(profileRepository.findByUid(tuple.getT1().getUid()))
                )
                .switchIfEmpty(handleUserNotFoundError(userId));

        return readResponse(userMono, Profile.class);
    }

    @NonNull
    public Mono<ServerResponse> unfollow(ServerRequest request) {
        String userId = request.pathVariable("userId");

        Mono<Profile> userMono = profileRepository.findByUid(userId)
                .zipWith(currentUserId())
                .flatMap(tuple ->
                     followRepository.findByIdAndTargetId(tuple.getT2(), tuple.getT1().getId())
                            .flatMap(followRelation ->
                                    followService.removeRelation(followRelation)
                                        .then(profileRepository.findByUid(tuple.getT1().getId()))
                            )
                            .switchIfEmpty(error(new DocumentNotFoundException("")))
                )
                .switchIfEmpty(handleUserNotFoundError(userId));

        return readResponse(userMono, Profile.class);
    }

    private Mono<Profile> handleUserNotFoundError(String userId) {
        return error(new DocumentNotFoundException("User with id: " + userId + " not found."));
    }

}
