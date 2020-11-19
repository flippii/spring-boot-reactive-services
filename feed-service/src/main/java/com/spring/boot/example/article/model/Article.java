package com.spring.boot.example.article.model;

import com.spring.boot.example.core.domain.AbstractDocument;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

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
    private String title;
    private String description;
    private String body;
    private Set<Tag> tags;

    public Article(String articleId, String slug, String title, String description, String body, Set<Tag> tags) {
        this.articleId = articleId;
        this.slug = slug;
        this.title = title;
        this.description = description;
        this.body = body;
        this.tags = tags;
    }

}
