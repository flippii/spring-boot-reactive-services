package com.spring.boot.example.profile.model;

import com.spring.boot.example.core.domain.AbstractDocument;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Document(collection = "profiles")
public class Profile extends AbstractDocument<String> {

    @Id
    private String id;

    @Indexed(name = "uid-index", unique = true)
    private String uid;
    private String firstName;
    private String lastName;
    private String bio;
    private String imageUrl;

    @CreatedDate
    @Field("created_date")
    private Instant createdDate;

    @LastModifiedDate
    @Field("last_modified_date")
    private Instant lastModifiedDate;

}
