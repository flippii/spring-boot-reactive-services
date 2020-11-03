package com.spring.boot.example.profile.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.Instant;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class ProfileDto {

    private String uid;
    private String firstName;
    private String lastName;
    private String email;
    private boolean activated = false;
    private String langKey;
    private String imageUrl;
    private Instant createdAt;
    private Instant lastModifiedDate;

}
