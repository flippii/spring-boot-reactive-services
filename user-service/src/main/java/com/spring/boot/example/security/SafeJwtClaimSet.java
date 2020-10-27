package com.spring.boot.example.security;

import com.nimbusds.jwt.JWTClaimsSet;

import java.text.ParseException;
import java.util.Date;
import java.util.Optional;
import java.util.function.Supplier;

import static java.util.Optional.ofNullable;

public final class SafeJwtClaimSet {

    private final JWTClaimsSet claimsSet;

    public SafeJwtClaimSet(JWTClaimsSet claimsSet) {
        this.claimsSet = claimsSet;
    }

    public Optional<String> getSubject() {
        return ofNullable(claimsSet.getSubject());
    }

    public Optional<String> getGivenName() {
        return getStringValue("given_name");
    }

    public Optional<String> getFamilyName() {
        return getStringValue("family_name");
    }

    public Optional<String> getEmail() {
        return getStringValue("email");
    }

    public Optional<String> getLangKey() {
        return getStringValue("langKey");
    }

    public Optional<String> getLocale() {
        return getStringValue("locale");
    }

    public Optional<String> gePicture() {
        return getStringValue("picture");
    }

    private Optional<String> getStringValue(String claim) {
        return error(() -> ofNullable(claimsSet.getStringClaim(claim))).get();
    }

    public Optional<Boolean> getEmailVerified() {
        return error(() -> ofNullable(claimsSet.getBooleanClaim("email_verified"))).get();
    }

    public Optional<Date> getUpdatedAt() {
        return error(() -> ofNullable(claimsSet.getDateClaim("updated_at"))).get();
    }

    @FunctionalInterface
    private interface ErrorSupplier<T, E extends Exception> {

        T get() throws E;

    }

    private static <T> Supplier<T> error(ErrorSupplier<T, ParseException> errorSupplier) {
        return () -> {
            try {
                return errorSupplier.get();
            } catch (ParseException ex) {
                throw new IllegalStateException(ex);
            }
        };
    }

}
