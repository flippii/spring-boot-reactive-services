package com.spring.boot.example.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@Data
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    private String secret;
    private int sessionTime;
    private List<String> authenticatedPathMatchers = new ArrayList<>();
    private String identityClaimLabel;

}
