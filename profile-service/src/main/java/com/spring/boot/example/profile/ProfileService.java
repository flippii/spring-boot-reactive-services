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

    public Mono<ProfileData> getById(String id, String uid) {
        return profileRepository.findById(id)
                .map(profile ->
                     new ProfileData()
                            .setId(profile.getId())
                            .setFirstName(profile.getFirstName())
                            .setLastName(profile.getLastName())
                            .setImageUrl(profile.getImageUrl())
                            .setFollowing(followRepository.existsByIdAndTargetId(uid, id))
                            .setCreatedAt(profile.getCreatedDate())
                            .setLastModifiedDate(profile.getLastModifiedDate())
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

    public Mono<Profile> update(String id, String uid, ProfileParams profileParams) {
        return profileRepository.findByIdAndUid(id, uid)
                .flatMap(profile -> {
                    profile.setFirstName(profileParams.getFirstName())
                            .setLastName(profileParams.getLastName())
                            .setBio(profileParams.getBio())
                            .setImageUrl(profileParams.getImageUrl());

                    return profileRepository.save(profile);
                });
    }

    public Mono<Profile> delete(String id, String uid) {
        return profileRepository.findByIdAndUid(id, uid)
                .flatMap(profile ->
                        profileRepository.delete(profile).then(just(profile))
                );
    }

}
