package com.clover.store.clover.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class CloverTopicConfig {

    @Value("${com.clover.store.messagequeueservice.config.kafka.topic-1}")
    private String topic;


    @Bean
    NewTopic cloverTopic(){
        return TopicBuilder.name(this.topic).replicas(1).build();
    }
}
