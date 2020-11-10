package com.spring.boot.example.follow;

import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "follow")
public class FollowProperties {

    @Getter
    private final Messaging messaging = new Messaging();

    @Data
    public static class Messaging {

        private long timeout;

    }

}
