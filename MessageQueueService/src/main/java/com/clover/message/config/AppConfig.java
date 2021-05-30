package com.clover.message.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties("spring.kafka")
@Data
public class AppConfig {

    private final AppConfig.Consumer consumer = new AppConfig.Consumer();
    private final AppConfig.Producer producer = new AppConfig.Producer();
    private List<String> topics;
    private String containerFactory;

    @Data
    public static class Consumer {
        private List<String> bootstrapServers;
        private String groupID;
        private String autoOffsetReset;
        private String keyDeserializer;
        private String valueDeserializer;

        public Consumer(){}
    }

    @Data
    public static class Producer {
        private List<String> bootstrapServers;
        private String keySerializer;
        private String valueSerializer;
        private String valueJsonSerializer;

        public Producer(){}
    }
}
