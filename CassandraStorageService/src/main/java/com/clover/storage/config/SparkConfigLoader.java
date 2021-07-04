package com.clover.storage.config;

import lombok.Data;

@Data
public class SparkConfigLoader {

    private SparkConfigLoader.driver driver = new driver();
    private SparkConfigLoader.worker worker = new worker();
    private SparkConfigLoader.executor executor = new executor();
    private SparkConfigLoader.rpc rpc = new rpc();
    private String master;
    private String localhost;
    private int port;
    private String appName;
    private String checkpoint;
    private String httpEntityName;
    private String statusCodeName;
    private SparkConfigLoader.cassandra cassandra = new cassandra();
    private SparkConfigLoader.constants constants = new constants();

    @Data
    public static class driver {
        private String memory;
        private String name;
    }

    @Data
    public static class worker {
        private String memory;
        private String name;
    }

    @Data
    public static class executor {
        private String memory;
        private String name;
    }


    @Data
    public static class rpc {
        private rpc.message message = new message();

        @Data
        public static class message {
            private String name;
            private String maxSize;
        }
    }


    @Data
    public static class cassandra {
        private String host;
        private String streamElasticSearchURI;
        private String durations;
        private String keyspace;
        private String table;
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