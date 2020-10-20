package com.spring.boot.example.core.springDoc;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "spring-docs")
public class SpringDocProperties {

    private String title = "Application API";
    private String description = "API documentation";
    private String version = "0.0.1";
    private String contactName;
    private String contactUrl;
    private String contactEmail;
    private String license;
    private String licenseUrl;
    private Server[] servers = {};

    @Data
    public static class Server {

        private String url;
        private String description;

    }

}
