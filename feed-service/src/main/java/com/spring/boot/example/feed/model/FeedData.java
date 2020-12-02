package com.spring.boot.example.feed.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Set;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class FeedData {

    private String id;
    private String userId;
    private String followId;
    private String articleId;
    private ArticleData article;
    private int articleSize;
    private Set<FavouriteData> articleFavourite;
    /*private String slug;
    private String title;
    private String description;
    private String body;
    private boolean favorited;
    private int favoritesCount;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private List<String> tagList;

    @JsonProperty("author")
    private ProfileData profileData;*/

}
