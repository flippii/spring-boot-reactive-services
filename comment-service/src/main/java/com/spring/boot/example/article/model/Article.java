package com.spring.boot.example.article.model;

import com.spring.boot.example.core.domain.AbstractDocument;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import nonapi.io.github.classgraph.json.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Document(collection = "articles")
public class Article extends AbstractDocument<String> {

    @Id
    private String id;

    @Indexed(name = "articleId-index", unique = true)
    private String articleId;

    @Indexed(name = "slug-index", unique = true)
    private String slug;

}
