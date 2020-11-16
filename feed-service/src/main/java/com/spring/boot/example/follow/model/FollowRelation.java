package com.spring.boot.example.follow.model;

import com.spring.boot.example.core.domain.AbstractDocument;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Document(collection = "user-follow")
@CompoundIndexes({
        @CompoundIndex(name = "user-target-index", def = "{'userId' : 1, 'targetId': 1}")
})
public class FollowRelation extends AbstractDocument<String> {

    @Indexed
    private String id;

    @Indexed(name = "user.index")
    private String userId;

    @Indexed(name = "target.index")
    private String targetId;

}
