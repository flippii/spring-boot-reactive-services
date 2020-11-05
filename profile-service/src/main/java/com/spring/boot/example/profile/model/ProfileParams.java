package com.spring.boot.example.profile.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class ProfileParams {

    private String firstName;
    private String lastName;
    private String bio;
    private String imageUrl;

}
