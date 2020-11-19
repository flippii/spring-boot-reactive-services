package com.spring.boot.example.follow.model;

import com.spring.boot.example.core.domain.AbstractDocument;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Document(collection = "user-follow")
@CompoundIndexes({
        @CompoundIndex(name = "user-target-index", def = "{'userId' : 1, 'targetId': 1}", unique = true)
})
public class FollowRelation extends AbstractDocument<String> {

    private String id;
    private String userId;
    private String targetId;

    public FollowRelation(String userId, String targetId) {
        this.userId = userId;
        this.targetId = targetId;
    }

}
