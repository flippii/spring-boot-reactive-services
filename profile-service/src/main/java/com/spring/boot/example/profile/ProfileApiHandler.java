package com.spring.boot.example.profile;

import br.com.fluentvalidator.context.ValidationResult;
import com.spring.boot.example.core.web.error.DocumentNotFoundException;
import com.spring.boot.example.core.web.error.ValidationException;
import com.spring.boot.example.profile.model.Profile;
import com.spring.boot.example.profile.model.ProfileParams;
import com.spring.boot.example.profile.model.ProfileData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

import static com.spring.boot.example.core.web.HandlerFunctions.readResponse;
import static com.spring.boot.example.core.web.HandlerFunctions.writeResponse;
import static com.spring.boot.example.security.ReactiveSecurityContextUtils.currentUserId;
import static reactor.core.publisher.Mono.error;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProfileApiHandler {

    private final ProfileService profileService;
    private final ProfileParamsValidator profileParamsValidator;

    @NonNull
    public Mono<ServerResponse> profile(ServerRequest request) {
        Mono<ProfileData> profileMono = currentUserId()
                .flatMap(uid ->
                        profileService.getByUid(uid)
                                .switchIfEmpty(error(new DocumentNotFoundException("Profile with uid \"" + uid + "\" not found.")))
                );

        return readResponse(profileMono, ProfileData.class);
    }

    @NonNull
    public Mono<ServerResponse> create(ServerRequest request) {
        Mono<Profile> profileMono = request.bodyToMono(ProfileParams.class)
                .zipWith(currentUserId())
                .doOnNext(tuple -> validateProfileRequest(tuple.getT1()))
                .flatMap(tuple ->
                        profileService.create(tuple.getT2(), tuple.getT1())
                                .doOnError(throwable -> error(new DocumentNotFoundException("Can't create new profile.", throwable)))
                );

        return writeResponse(profileMono, (profile) -> URI.create("/api/profiles/" + profile.getId()));
    }

    @NonNull
    public Mono<ServerResponse> update(ServerRequest request) {
        Mono<Profile> profileMono = request.bodyToMono(ProfileParams.class)
                .zipWith(currentUserId())
                .doOnNext(tuple -> validateProfileRequest(tuple.getT1()))
                .flatMap(tuple ->
                        profileService.update(tuple.getT2(), tuple.getT1())
                                .doOnError(throwable -> error(new DocumentNotFoundException("Can't update profile with uid \"" + tuple.getT2() + "\".")))
                );

        return readResponse(profileMono, Profile.class);
    }

    private void validateProfileRequest(ProfileParams profileParams) {
        final ValidationResult result = profileParamsValidator.validate(profileParams);

        if (!result.isValid()) {
            throw new ValidationException("Profile not saved, invalid data send.", result.getErrors());
        }
    }

    @NonNull
    public Mono<ServerResponse> delete(ServerRequest request) {
        Mono<Profile> profileMono = currentUserId()
                .flatMap(uid ->
                        profileService.delete(uid)
                                .doOnError(throwable -> error(new DocumentNotFoundException("Can't delete profile with uid \"" + uid + "\".", throwable)))
                );

        return readResponse(profileMono, Profile.class);
    }

}
