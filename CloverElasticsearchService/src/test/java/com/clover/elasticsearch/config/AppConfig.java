package com.clover.elasticsearch.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
public class AppConfig {

    @Bean
    @ConfigurationProperties("elasticsearch")
    public ElasticSearchYMLConfig YMLConfig() {
        return new ElasticSearchYMLConfig();
    }
}
