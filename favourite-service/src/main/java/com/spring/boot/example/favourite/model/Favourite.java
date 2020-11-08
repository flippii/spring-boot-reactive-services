package com.spring.boot.example.favourite.model;

import com.spring.boot.example.core.domain.AbstractDocument;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Document(collection = "article-favourite")
public class Favourite extends AbstractDocument<String> {

    @Indexed(name = "article.index")
    private String id;
    private String userId;

    public Favourite(String id, String userId) {
        this.id = id;
        this.userId = userId;
    }

}
