package com.spring.boot.example.user.mapper;

import com.spring.boot.example.security.SafeJwtClaimSet;
import com.spring.boot.example.user.model.User;
import com.spring.boot.example.user.model.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private static final String DEFAULT_LANGUAGE = "en";

    public User map(SafeJwtClaimSet claimsSet) {
        User user = new User()
                .setActivated(true);

        claimsSet.getSubject()
                .ifPresentOrElse(user::setUid, () -> {
                    throw new IllegalStateException("Subject in jwt token is not available.");
                });

        claimsSet.getGivenName()
                .ifPresent(user::setFirstName);

        claimsSet.getFamilyName()
                .ifPresent(user::setLastName);

        claimsSet.getEmailVerified()
                .ifPresent(user::setActivated);

        claimsSet.getEmail()
                .ifPresent(email -> user.setEmail(email.toLowerCase()));

        claimsSet.getLangKey()
                .ifPresentOrElse(user::setLangKey, () -> setLocaleOrDefault(claimsSet, user));

        claimsSet.gePicture()
                .ifPresent(user::setImageUrl);

        return user;
    }

    private void setLocaleOrDefault(SafeJwtClaimSet claimsSet, User user)  {
        claimsSet.getLocale().ifPresentOrElse(
                locale -> {
                    if (locale.contains("_")) {
                        locale = locale.substring(0, locale.indexOf('_'));
                    } else if (locale.contains("-")) {
                        locale = locale.substring(0, locale.indexOf('-'));
                    }

                    user.setLangKey(locale.toLowerCase());
                }, () -> user.setLangKey(DEFAULT_LANGUAGE)
        );
    }

    public UserResponse map(User user) {
        return new UserResponse()
                .setUid(user.getId())
                .setFirstName(user.getFirstName())
                .setLastName(user.getLastName())
                .setEmail(user.getEmail())
                .setActivated(user.isActivated())
                .setLangKey(user.getLangKey())
                .setImageUrl(user.getImageUrl())
                .setCreatedAt(user.getCreatedDate())
                .setLastModifiedDate(user.getLastModifiedDate());
    }

}
