package com.spring.boot.example.follow.model;

import com.spring.boot.example.core.domain.AbstractDocument;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Document(collection = "user-follow")
public class FollowRelation extends AbstractDocument<String> {

    @Id
    private String id;

    @Indexed(name = "target.index")
    private String targetId;

    public FollowRelation(String id, String targetId) {
        this.id = id;
        this.targetId = targetId;
    }

}
