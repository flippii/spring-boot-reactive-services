package com.spring.boot.example.article.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ArticleParams {

    private String title;
    private String description;
    private String body;
    private String[] tags;

}
