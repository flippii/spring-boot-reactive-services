package com.spring.boot.example.article.model;

import com.spring.boot.example.core.domain.AbstractAuditingDocument;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "articles")
public class Article extends AbstractAuditingDocument<String> {

    @Id
    private String id;
    private String slug;
    private String title;
    private String description;
    private String body;
    private List<Tag> tags;

    public Article(String title, String description, String body, String[] tagList) {
        this.slug = toSlug(title);
        this.title = title;
        this.description = description;
        this.body = body;
        this.tags = Arrays.stream(tagList)
                .collect(toSet())
                .stream()
                .map(Tag::new)
                .collect(toList());
    }

    private String toSlug(String title) {
        return title.toLowerCase().replaceAll("[\\&|[\\uFE30-\\uFFA0]|\\’|\\”|\\s\\?\\,\\.]+", "-");
    }

}


