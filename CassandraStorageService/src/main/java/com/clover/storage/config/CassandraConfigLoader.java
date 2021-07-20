package com.clover.storage.config;


import lombok.Data;

@Data
public class CassandraConfigLoader {
    private CassandraConfigLoader.cassandra cassandra = new CassandraConfigLoader.cassandra();

    @Data
    public static class cassandra {
        private String contactPoints;
        private String port;
        private String keyspace;
        private int replicationFactor;
        private String localDatacenter;
        private String duration;
        private boolean durableWrites;
        private String schema;
        private String tableName;
    }
}
