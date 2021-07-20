package com.clover.data.config;

import lombok.Data;

@Data
public class DataSourceConfigLoader {
    private DataSourceConfigLoader.constants constants = new constants();
    private DataSourceConfigLoader.KafkaConfig kafkaConfig = new KafkaConfig();
    private DataSourceConfigLoader.MongoConfig mongoConfig = new MongoConfig();
    private String localhost;
    private int port;

    @Data
    public static class MongoConfig {
        String connectionString;
        String databaseName;
    }


    @Data
    public static class KafkaConfig {
        private String url;
    }

    @Data
    public static class constants {
        private constants.timeouts timeouts = new timeouts();
        private constants.connectionPool connectionPool = new connectionPool();
        private constants.executor executor = new executor();

        @Data
        public static class timeouts {
            private Integer connectionTimeout;
            private Integer connectionRequestTimeout;
            private Integer socketTimeout;
            private Integer defaultKeepAliveTime;
            private Integer completableFutureTimeoutValue;
            private Integer cassandraInsertTimeout;
            private Integer sparkStreamingContextTimeout;

        }

        @Data
        public static class connectionPool {
            private Integer maxRouteConnections;
            private Integer maxTotalConnections;
            private Integer maxLocalHostConnections;
            private Integer defaultKeepAliveTime;
            private Integer completableFutureTimeoutValue;
        }

        @Data
        public static class executor {
            private String threadNamePrefix;
        }
    }
}