package com.clover.spark.Config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;


@Data
public class SparkConfigLoader {

    private SparkConfigLoader.driver driver = new SparkConfigLoader.driver();
    private SparkConfigLoader.worker worker = new SparkConfigLoader.worker();
    private SparkConfigLoader.executor executor = new SparkConfigLoader.executor();
    private SparkConfigLoader.rpc rpc = new SparkConfigLoader.rpc();
    private SparkConfigLoader.kafka kafka = new SparkConfigLoader.kafka();
    private String master;
    private String localhost;
    private String checkpoint;
    private SparkConfigLoader.cassandra cassandra = new SparkConfigLoader.cassandra();


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
    }
}