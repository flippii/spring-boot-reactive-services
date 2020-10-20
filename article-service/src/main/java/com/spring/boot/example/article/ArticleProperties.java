package com.spring.boot.example.article;

import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "article")
public class ArticleProperties {

    @Getter
    private final Messaging messaging = new Messaging();

    @Data
    public static class Messaging {

        private long timeout;

    }

}
