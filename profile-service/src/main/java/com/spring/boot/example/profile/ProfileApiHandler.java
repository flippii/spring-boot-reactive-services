package com.spring.boot.example.profile;

import br.com.fluentvalidator.context.ValidationResult;
import com.spring.boot.example.core.web.error.DocumentNotFoundException;
import com.spring.boot.example.core.web.error.ValidationException;
import com.spring.boot.example.profile.model.Profile;
import com.spring.boot.example.profile.model.ProfileParams;
import com.spring.boot.example.profile.model.ProfileData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    public Mono<ServerResponse> profile(ServerRequest request) {
        String id = request.pathVariable("id");

        Mono<ProfileData> profileMono = currentUserId()
                .flatMap(uid ->
                        profileService.getById(id, uid)
                                .switchIfEmpty(error(new DocumentNotFoundException("Profile with id \"" + id + "\" not found.")))
                );

        return readResponse(profileMono, ProfileData.class);
    }

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

    public Mono<ServerResponse> update(ServerRequest request) {
        String id = request.pathVariable("id");

        Mono<Profile> profileMono = request.bodyToMono(ProfileParams.class)
                .zipWith(currentUserId())
                .doOnNext(tuple -> validateProfileRequest(tuple.getT1()))
                .flatMap(tuple ->
                        profileService.update(id, tuple.getT2(), tuple.getT1())
                                .doOnError(throwable -> error(new DocumentNotFoundException("Can't update profile with id \"" + id + "\".")))
                );

        return readResponse(profileMono, Profile.class);
    }

    private void validateProfileRequest(ProfileParams profileParams) {
        final ValidationResult result = profileParamsValidator.validate(profileParams);

        if (!result.isValid()) {
            throw new ValidationException("Profile not saved, invalid data send.", result.getErrors());
        }
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        String id = request.pathVariable("id");

        Mono<Profile> profileMono = currentUserId()
                .flatMap(uid ->
                        profileService.delete(uid, id)
                                .doOnError(throwable -> error(new DocumentNotFoundException("Can't delete profile with id \"" + id + "\".", throwable)))
                );

        return readResponse(profileMono, Profile.class);
    }

}
