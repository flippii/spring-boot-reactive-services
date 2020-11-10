package com.spring.boot.example.favourite;

import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "favourite")
public class FavouriteProperties {

    @Getter
    private final Messaging messaging = new Messaging();

    @Data
    public static class Messaging {

        private long timeout;

    }

}
