package com.clover.message.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class CloverTopicConfig {

    @Autowired
    private AppConfig appConfig;

    @Bean
    NewTopic cloverTopic(){
        return TopicBuilder.name(appConfig.getTopics().get(0)).replicas(1).build();
    }
}
