package com.clover.spark.Config;

import lombok.Data;

@Data
public class SparkConfigLoader {

    private SparkConfigLoader.driver driver = new SparkConfigLoader.driver();
    private SparkConfigLoader.worker worker = new SparkConfigLoader.worker();
    private SparkConfigLoader.executor executor = new SparkConfigLoader.executor();
    private SparkConfigLoader.rpc rpc = new SparkConfigLoader.rpc();
    private SparkConfigLoader.kafka kafka = new SparkConfigLoader.kafka();
    private String master;
    private String localhost;
    private int port;
    private String appName;
    private String checkpoint;
    private String httpEntityName;
    private String statusCodeName;
    private SparkConfigLoader.cassandra cassandra = new SparkConfigLoader.cassandra();
    private SparkConfigLoader.constants constants = new SparkConfigLoader.constants();

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
        private rpc.message message = new rpc.message();

        @Data
        public static class message {
            private String name;
            private String maxSize;
        }
    }

    @Data
    public static class kafka {
        private String topics;
        private kafka.Stream stream = new kafka.Stream();
        private kafka.broker broker = new kafka.broker();

        @Data
        public static class Stream {
            private String durations;
        }

        @Data
        public static class broker {
            private String name;
            private String list;
        }
    }

    @Data
    public static class cassandra {
        private String host;
        private String streamInsertURI;
        private String durations;
        private String keyspace;
        private String table;
    }

    @Data
    public static class constants {
        private constants.timeouts timeouts = new constants.timeouts();
        private constants.connectionPool connectionPool = new constants.connectionPool();
        private constants.executor executor = new constants.executor();

        @Data
        public static class timeouts {
            private Integer connectionTimeout;
            private Integer connectionRequestTimeout;
            private Integer socketTimeout;
            private Integer defaultKeepAliveTime;
            private Integer completableFutureTimeoutValue;

        }

        @Data
        public static class connectionPool {
            private Integer maxRouteConnections;
            private Integer maxTotalConnections;
            private Integer maxLocalHostConnections;
        }

        @Data
        public static class executor {
            private String threadNamePrefix;
        }
    }
}