package ru.otus.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Main configuration class.
 */
@Configuration
@ComponentScan(basePackages = "ru.otus.app")
public class AppConfig {
    @Bean
    public ProductProperties productProperties() {
        return ProductProperties.create("products.yml");
    }
}
