package com.spring.boot.example.article.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ArticleDto {

    private String title;
    private String description;
    private String body;

}
