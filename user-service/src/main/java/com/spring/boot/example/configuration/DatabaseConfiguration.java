package com.spring.boot.example.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.mapping.event.ValidatingMongoEventListener;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.List;

import static com.spring.boot.example.core.converter.JSR310DateConverters.DateToZonedDateTimeConverter;
import static com.spring.boot.example.core.converter.JSR310DateConverters.ZonedDateTimeToDateConverter;

@Configuration
@EnableMongoAuditing
public class DatabaseConfiguration {

    @Bean
    public ValidatingMongoEventListener validatingMongoEventListener() {
        return new ValidatingMongoEventListener(validator());
    }

    @Bean
    public LocalValidatorFactoryBean validator() {
        return new LocalValidatorFactoryBean();
    }

    @Bean
    public MongoCustomConversions customConversions() {
        return new MongoCustomConversions(
                List.of(DateToZonedDateTimeConverter.INSTANCE, ZonedDateTimeToDateConverter.INSTANCE));
    }

}
