package com.clover.message.config;

import com.clover.message.model.Product;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

//https://reflectoring.io/spring-boot-kafka/
@Configuration
public class CloverConsumerConfig {

    @Autowired
    private AppConfig appConfig;

    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, appConfig.getConsumer().getBootstrapServers().get(0));
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, appConfig.getConsumer().getKeyDeserializer());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, appConfig.getConsumer().getGroupID());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, appConfig.getConsumer().getAutoOffsetReset());
        return props;
    }

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, Product> ProductConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs(), new StringDeserializer(),
                new JsonDeserializer<>(Product.class));
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, Product>> ProductKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Product> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(ProductConsumerFactory());
        return factory;
    }
}
