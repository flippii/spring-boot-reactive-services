package com.spring.boot.example.user.model;

import com.spring.boot.example.core.domain.AbstractAuditingDocument;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "user")
public class User extends AbstractAuditingDocument<String>  {

    @Id
    private String id;
    private String email;
    private String username;
    private String password;
    private String bio;
    private String image;

    public User(String email, String username, String password, String bio, String image) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.bio = bio;
        this.image = image;
    }

}
