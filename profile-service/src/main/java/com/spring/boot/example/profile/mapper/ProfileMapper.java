package com.spring.boot.example.profile.mapper;

import com.spring.boot.example.profile.model.Profile;
import com.spring.boot.example.profile.model.ProfileDto;
import org.springframework.stereotype.Component;

@Component
public class ProfileMapper {

    public ProfileDto map(Profile profile) {
        return new ProfileDto()
                .setUid(profile.getId())
                .setFirstName(profile.getFirstName())
                .setLastName(profile.getLastName())
                .setEmail(profile.getEmail())
                .setActivated(profile.isActivated())
                .setLangKey(profile.getLangKey())
                .setImageUrl(profile.getImageUrl())
                .setCreatedAt(profile.getCreatedDate())
                .setLastModifiedDate(profile.getLastModifiedDate());
    }

}
