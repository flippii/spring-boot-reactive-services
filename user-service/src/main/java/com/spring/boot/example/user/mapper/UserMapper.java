package com.spring.boot.example.user.mapper;

import com.nimbusds.jwt.JWTClaimsSet;
import com.spring.boot.example.user.model.User;
import com.spring.boot.example.user.model.UserDto;
import lombok.SneakyThrows;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Map;
import java.util.Optional;

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

        if (details.get("family_name") != null) {
            user.setLastName((String) details.get("family_name"));
        }

        if (details.get("email_verified") != null) {
            user.setActivated((Boolean) details.get("email_verified"));
        }

        if (details.get("email") != null) {
            user.setEmail(((String) details.get("email")).toLowerCase());
        } else {
            user.setEmail((String) details.get("sub"));
        }

        if (details.get("langKey") != null) {
            user.setLangKey((String) details.get("langKey"));
        } else if (details.get("locale") != null) {
            String locale = (String) details.get("locale");

            if (locale.contains("_")) {
                locale = locale.substring(0, locale.indexOf('_'));
            } else if (locale.contains("-")) {
                locale = locale.substring(0, locale.indexOf('-'));
            }

            user.setLangKey(locale.toLowerCase());
        } else {
            user.setLangKey(DEFAULT_LANGUAGE);
        }

        if (details.get("picture") != null) {
            user.setImageUrl((String) details.get("picture"));
        }

        return user;
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
