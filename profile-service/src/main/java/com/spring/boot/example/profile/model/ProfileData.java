package com.spring.boot.example.profile.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.Instant;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class ProfileData {

    private String id;
    private String firstName;
    private String lastName;
    private String bio;
    private String imageUrl;
    private boolean following;
    private Instant createdAt;
    private Instant lastModifiedDate;

}
