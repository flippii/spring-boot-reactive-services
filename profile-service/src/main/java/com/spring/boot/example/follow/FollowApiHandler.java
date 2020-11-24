package com.spring.boot.example.follow;

import com.spring.boot.example.core.web.error.DocumentNotFoundException;
import com.spring.boot.example.follow.model.FollowRelation;
import com.spring.boot.example.profile.ProfileService;
import com.spring.boot.example.profile.model.ProfileData;
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
    private final ProfileService profileService;
    private final FollowRepository followRepository;

    @NonNull
    public Mono<ServerResponse> follow(ServerRequest request) {
        String userId = request.pathVariable("userId");

        Mono<ProfileData> profileMono = profileService.findByUid(userId)
                .zipWith(currentUserId())
                .flatMap(tuple ->
                        followService.saveRelation(new FollowRelation(tuple.getT2(), tuple.getT1().getUid()))
                                .then(profileService.getByUid(tuple.getT1().getUid()))
                )
                .switchIfEmpty(handleUserNotFoundError(userId));

        return readResponse(profileMono, ProfileData.class);
    }

    @NonNull
    public Mono<ServerResponse> unfollow(ServerRequest request) {
        String userId = request.pathVariable("userId");

        Mono<ProfileData> profileMono = profileService.findByUid(userId)
                .zipWith(currentUserId())
                .flatMap(tuple ->
                     followRepository.findByUserIdAndFollowId(tuple.getT2(), tuple.getT1().getUid())
                            .flatMap(followRelation ->
                                    followService.removeRelation(followRelation)
                                        .then(profileService.getByUid(tuple.getT1().getUid()))
                            )
                )
                .switchIfEmpty(handleUserNotFoundError(userId));

        return readResponse(profileMono, ProfileData.class);
    }

    private Mono<ProfileData> handleUserNotFoundError(String userId) {
        return error(new DocumentNotFoundException("Profile with uid: " + userId + " not found."));
    }

}
