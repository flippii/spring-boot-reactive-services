package com.spring.boot.example.core.starter;

import com.spring.boot.example.core.springDoc.DefaultSpringDocCustomizer;
import com.spring.boot.example.core.springDoc.SpringDocCustomizer;
import com.spring.boot.example.core.springDoc.SpringDocProperties;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StopWatch;

import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
@ConditionalOnWebApplication
@ConditionalOnClass({Info.class, OpenAPI.class})
@EnableConfigurationProperties(SpringDocProperties.class)
public class SpringDocAutoConfiguration {

    private final SpringDocProperties springDocProperties;

    @Bean
    @ConditionalOnMissingBean(name = "springDocOpenAPI")
    public OpenAPI springDocOpenAPI(List<SpringDocCustomizer> springDocCustomizers) {
        log.debug("Starting OpenAPI docs");
        StopWatch watch = new StopWatch();
        watch.start();

        OpenAPI openAPI = new OpenAPI();

        springDocCustomizers.forEach(customizer -> customizer.customize(openAPI));

        watch.stop();
        log.debug("Started OpenAPI docs in {} ms", watch.getTotalTimeMillis());

        return openAPI;
    }

    @Bean
    public DefaultSpringDocCustomizer defaultSpringDocCustomizer() {
        return new DefaultSpringDocCustomizer(springDocProperties);
    }

}
