package com.spring.boot.example.profile;

import com.spring.boot.example.follow.FollowRepository;
import com.spring.boot.example.profile.model.Profile;
import com.spring.boot.example.profile.model.ProfileParams;
import com.spring.boot.example.profile.model.ProfileData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static reactor.core.publisher.Mono.just;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final FollowRepository followRepository;

    public Mono<ProfileData> getById(String uid) {
        return profileRepository.findByUid(uid)
                .map(profile ->
                        new ProfileData()
                                .setId(profile.getId())
                                .setFirstName(profile.getFirstName())
                                .setLastName(profile.getLastName())
                                .setImageUrl(profile.getImageUrl())
                                .setCreatedAt(profile.getCreatedDate())
                                .setLastModifiedDate(profile.getLastModifiedDate()))
                .flatMap(profileData ->
                        followRepository.findById(uid)
                                .doOnSuccess(followRelation -> profileData.setFollowing(followRelation != null))
                                .then(just(profileData))
                );
    }

    public Mono<Profile> create(String uid, ProfileParams profileParams) {
        Profile profile = new Profile()
                .setUid(uid)
                .setFirstName(profileParams.getFirstName())
                .setLastName(profileParams.getLastName())
                .setBio(profileParams.getBio())
                .setImageUrl(profileParams.getImageUrl());

        return profileRepository.save(profile);
    }

    public Mono<Profile> update(String uid, ProfileParams profileParams) {
        return profileRepository.findByUid(uid)
                .flatMap(profile -> {
                    profile.setFirstName(profileParams.getFirstName())
                            .setLastName(profileParams.getLastName())
                            .setBio(profileParams.getBio())
                            .setImageUrl(profileParams.getImageUrl());

                    return profileRepository.save(profile);
                });
    }

    public Mono<Profile> delete(String uid) {
        return profileRepository.findByUid(uid)
                .flatMap(profile -> profileRepository.delete(profile).then(just(profile)));
    }

}
