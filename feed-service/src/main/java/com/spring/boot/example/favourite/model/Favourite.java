package com.spring.boot.example.favourite.model;

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
@Document(collection = "article-favourite")
@CompoundIndexes({
        @CompoundIndex(name = "article-user-index", def = "{'articleId' : 1, 'userId': 1}", unique = true)
})
public class Favourite extends AbstractDocument<String> {

    @Indexed(name = "id-index", unique = true)
    private String id;
    private String articleId;
    private String userId;

}
