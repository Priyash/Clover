package com.clover.data.config;

import com.mongodb.ConnectionString;
import lombok.extern.slf4j.Slf4j;
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

    //https://stackoverflow.com/questions/46430775/unable-to-create-a-database-using-mongo-java-drivers
    @Bean
    public MongoDatabaseFactory databaseFactory(){
        try {
            ConnectionString connectionString = new ConnectionString("mongodb://localhost:27017/PRODUCT");
            MongoDatabaseFactory databaseFactory = new SimpleMongoClientDatabaseFactory(connectionString);
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
