package com.spring.boot.example.article.model;

import com.spring.boot.example.core.domain.AbstractAuditingDocument;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Document(collection = "articles")
public class Article extends AbstractAuditingDocument<String> {

    private static final String SLUG_PATTERN = "[\\&|[\\uFE30-\\uFFA0]|\\’|\\”|\\s\\?\\,\\.]+";

    @Id
    private String id;
    private String slug;
    private String title;
    private String description;
    private String body;
    private Set<Tag> tags;

    public Article(String title, String description, String body, Set<Tag> tags) {
        this.slug = toSlug(title);
        this.title = title;
        this.description = description;
        this.body = body;
        this.tags = tags;
    }

    private String toSlug(String title) {
        return title.toLowerCase().replaceAll(SLUG_PATTERN, "-");
    }

}


