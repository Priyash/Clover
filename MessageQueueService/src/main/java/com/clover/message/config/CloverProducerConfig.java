package com.clover.message.config;

import com.clover.message.main.App;
import com.clover.message.model.Product;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloverProducerConfig {
    @Autowired
    private AppConfig appConfig;

    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                appConfig.getProducer().getBootstrapServers().get(0));
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                appConfig.getProducer().getKeySerializer());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                appConfig.getProducer().getValueSerializer());
        return props;
    }

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public Map<String, Object> productProducerConfigs(){
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, appConfig.getProducer().getBootstrapServers().get(0));
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, appConfig.getProducer().getKeySerializer());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, appConfig.getProducer().getValueJsonSerializer());
        return props;
    }

    @Bean
    public ProducerFactory<String, Product> ProductProducerFactory() {
        return new DefaultKafkaProducerFactory<>(productProducerConfigs());
    }

    @Bean
    public KafkaTemplate<String, Product> ProductKafkaTemplate() {
        return new KafkaTemplate<String, Product>(ProductProducerFactory());
    }
}
