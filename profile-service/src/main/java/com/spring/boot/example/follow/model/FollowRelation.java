package com.spring.boot.example.follow.model;

import com.spring.boot.example.core.domain.AbstractDocument;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Document(collection = "user-follow")
@CompoundIndexes({
        @CompoundIndex(name = "user-follow-index", def = "{'userId' : 1, 'followId': 1}", unique = true)
})
public class FollowRelation extends AbstractDocument<String> {

    @Id
    private String id;
    private String userId;
    private String followId;

    public FollowRelation(String userId, String followId) {
        this.userId = userId;
        this.followId = followId;
    }

}
