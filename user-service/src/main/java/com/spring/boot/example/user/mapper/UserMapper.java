package com.spring.boot.example.user.mapper;

import com.nimbusds.jwt.JWTClaimsSet;
import com.spring.boot.example.user.model.User;
import com.spring.boot.example.user.model.UserDto;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.text.ParseException;

import static java.util.Optional.ofNullable;

@Component
public class UserMapper {

    private static final String DEFAULT_LANGUAGE = "en";

    @SneakyThrows(value = ParseException.class)
    public User map(JWTClaimsSet claimsSet) {
        User user = new User()
                .setUid(claimsSet.getSubject())
                .setActivated(true);

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

        return user;
    }

    @SneakyThrows(value = ParseException.class)
    private void setLocaleOrDefault(JWTClaimsSet claimsSet, User user) {
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
