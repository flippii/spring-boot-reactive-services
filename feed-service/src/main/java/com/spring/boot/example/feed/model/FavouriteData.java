package com.spring.boot.example.feed.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavouriteData {

    private String id;
    private String articleId;
    private String userId;

}
