package com.spring.boot.example.feed.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class FeedData {

    private String id;
    private String slug;
    private String title;
    private String description;
    private String body;
    private boolean favorited;
    private int favoritesCount;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private List<String> tagList;

    @JsonProperty("author")
    private ProfileData profileData;

}
