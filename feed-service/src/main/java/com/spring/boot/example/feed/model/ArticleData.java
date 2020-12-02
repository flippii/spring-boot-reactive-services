package com.spring.boot.example.feed.model;

import com.spring.boot.example.article.model.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleData {

    private String id;
    private String articleId;
    private String slug;
    private String title;
    private String description;
    private String body;
    private Set<Tag> tags;

}
