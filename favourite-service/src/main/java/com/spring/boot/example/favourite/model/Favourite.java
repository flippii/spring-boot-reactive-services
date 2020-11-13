package com.spring.boot.example.favourite.model;

import com.spring.boot.example.core.domain.AbstractDocument;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Document(collection = "article-favourite")
@CompoundIndexes({
        @CompoundIndex(name = "article-user-index", def = "{'articleId' : 1, 'userId': 1}")
})
public class Favourite extends AbstractDocument<String> {

    @Id
    private String id;

    @Indexed(name = "article.index")
    private String articleId;

    @Indexed(name = "user.index")
    private String userId;

    public Favourite(String articleId, String userId) {
        this.articleId = articleId;
        this.userId = userId;
    }

}
