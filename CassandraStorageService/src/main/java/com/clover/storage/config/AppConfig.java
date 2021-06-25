package com.clover.storage.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
public class AppConfig {

    @Bean
    @ConfigurationProperties("config")
    public CassandraConfigLoader cassandraConfigLoader(){
        return new CassandraConfigLoader();
    }

    @Bean
    @ConfigurationProperties("spark")
    public SparkConfigLoader sparkConfigLoader(){
        return new SparkConfigLoader();
    }
}
