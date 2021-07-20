package com.clover.data.config;

import com.mongodb.ConnectionString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

@Configuration
@Slf4j
@ComponentScan(basePackages = "com.clover.data")
public class MongoConfig {

    @Autowired
    private DataSourceConfigLoader configLoader;

    //https://stackoverflow.com/questions/46430775/unable-to-create-a-database-using-mongo-java-drivers
    @Bean
    public MongoDatabaseFactory databaseFactory(){
        try {
            String mongoConnURL = configLoader.getMongoConfig().getConnectionString();
            String mongoDBName = configLoader.getMongoConfig().getDatabaseName();
            String connString = mongoConnURL + mongoDBName;

            //ConnectionString conn = new ConnectionString("mongodb://localhost:27017/PRODUCT");
            ConnectionString conn = new ConnectionString(connString);
            MongoDatabaseFactory databaseFactory = new SimpleMongoClientDatabaseFactory(connString);
            return databaseFactory;
        } catch (Exception ex) {
            log.error("Exception while instantiating mongo database factory ", ex);
        }
        return null;
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(databaseFactory());
    }
}
