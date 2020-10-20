package com.spring.boot.example.core.springDoc;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class DefaultSpringDocCustomizer implements SpringDocCustomizer, Ordered {

    public static final int DEFAULT_ORDER = 0;

    private final SpringDocProperties springDocProperties;

    @Override
    public void customize(OpenAPI openAPI) {
        Info info = new Info()
                .title(springDocProperties.getTitle())
                .version(springDocProperties.getVersion())
                .description(springDocProperties.getDescription())
                .contact(new Contact()
                        .name(springDocProperties.getContactName())
                        .email(springDocProperties.getContactEmail())
                        .url(springDocProperties.getContactUrl())
                )
                .license(new License()
                        .name(springDocProperties.getLicense())
                        .url(springDocProperties.getLicenseUrl())
                );

        List<Server> servers = Arrays.stream(springDocProperties.getServers())
                .map(server ->
                        new Server()
                                .description(server.getDescription())
                                .url(server.getUrl())
                )
                .collect(Collectors.toList());

        openAPI
                .components(
                        new Components()
                                .addSecuritySchemes("basicScheme", new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("basic"))
                )
                .info(info)
                .servers(servers);
    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }

}
