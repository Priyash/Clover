package com.clover.elasticsearch.config;

import lombok.Data;

@Data
public class ElasticSearchYMLConfig {
    private ElasticSearchYMLConfig.config config = new ElasticSearchYMLConfig.config();
    private ElasticSearchYMLConfig.index index = new ElasticSearchYMLConfig.index();

    @Data
    public static class config {
        private String localhost;
        private int port;
        private String mode;
    }

    @Data
    public static class index {
        private String indexName;
        private String indexNumberOfShardsKey;
        private String indexNumberOfShardsValue;
        private String indexNumberOfReplicaKey;
        private String indexNumberOfReplicaValue;
    }
}
