package com.spring.boot.example.core.starter;

import com.spring.boot.example.core.initializer.DataInitializer;
import com.spring.boot.example.core.initializer.DataInitializerInvoker;
import com.spring.boot.example.core.initializer.NoOpDataInitializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DataInitializerAutoConfiguration {

    @Bean
    public DataInitializerInvoker dataInitializerInvoker(List<DataInitializer> initializers) {
        return new DataInitializerInvoker(initializers);
    }

    @Bean
    @ConditionalOnMissingBean(value = DataInitializer.class, search = SearchStrategy.CURRENT)
    public DataInitializer NoOpDataInitializer() {
        return new NoOpDataInitializer();
    }

}
