package com.spring.boot.example.article.model;

import com.spring.boot.example.core.domain.AbstractDocument;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Document(collection = "articles")
public class Article extends AbstractDocument<String> {

    private String id;
    private String slug;
    private String title;
    private String description;
    private String body;

}