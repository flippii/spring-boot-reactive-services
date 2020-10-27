package com.spring.boot.example.user.model;

import com.spring.boot.example.core.domain.AbstractAuditingDocument;
import com.spring.boot.example.core.domain.AbstractDocument;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Document(collection = "user")
public class User extends AbstractDocument<String> {

    @Id
    private String id;
    private String uid;
    private String firstName;
    private String lastName;
    private String email;
    private boolean activated = false;
    private String langKey;
    private String imageUrl;

    @CreatedDate
    @Field("created_date")
    private Instant createdDate;

    @LastModifiedDate
    @Field("last_modified_date")
    private Instant lastModifiedDate;

}
