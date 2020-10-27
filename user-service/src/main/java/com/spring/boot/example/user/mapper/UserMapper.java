package com.spring.boot.example.user.mapper;

import com.nimbusds.jwt.JWTClaimsSet;
import com.spring.boot.example.user.model.User;
import com.spring.boot.example.user.model.UserDto;
import org.springframework.stereotype.Component;

import java.text.ParseException;

import static java.util.Optional.ofNullable;

@Component
public class UserMapper {

    private static final String DEFAULT_LANGUAGE = "en";

    public User map(JWTClaimsSet claimsSet) {
        User user = new User()
                .setUid(claimsSet.getSubject())
                .setActivated(true);

        try {
            ofNullable(claimsSet.getStringClaim("given_name"))
                    .ifPresent(user::setFirstName);

            ofNullable(claimsSet.getStringClaim("family_name"))
                    .ifPresent(user::setLastName);

            ofNullable(claimsSet.getBooleanClaim("email_verified"))
                    .ifPresent(user::setActivated);

            ofNullable(claimsSet.getStringClaim("email"))
                    .ifPresentOrElse(email -> user.setEmail(email.toLowerCase()), () -> user.setEmail(claimsSet.getSubject()));

            ofNullable(claimsSet.getStringClaim("langKey"))
                    .ifPresentOrElse(user::setLangKey, () -> setLocaleOrDefault(claimsSet, user));

            ofNullable(claimsSet.getStringClaim("picture"))
                    .ifPresent(user::setImageUrl);
        } catch (ParseException ex) {
            throw new IllegalStateException(ex);
        }

        return user;
    }

    private void setLocaleOrDefault(JWTClaimsSet claimsSet, User user)  {
        try {
            ofNullable(claimsSet.getStringClaim("locale"))
                    .ifPresentOrElse(
                            locale -> {
                                if (locale.contains("_")) {
                                    locale = locale.substring(0, locale.indexOf('_'));
                                } else if (locale.contains("-")) {
                                    locale = locale.substring(0, locale.indexOf('-'));
                                }

                                user.setLangKey(locale.toLowerCase());
                            }, () -> user.setLangKey(DEFAULT_LANGUAGE)
                    );
        } catch (ParseException ex) {
            throw new IllegalStateException(ex);
        }
    }

    public UserDto map(User user) {
        return new UserDto()
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
