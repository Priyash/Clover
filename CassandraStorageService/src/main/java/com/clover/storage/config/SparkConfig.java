package com.clover.storage.config;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SparkConfig {

    @Autowired
    private SparkConfigLoader sparkConfigLoader;

    @Bean
    @ConditionalOnMissingBean(SparkConf.class)
    public SparkConf sparkConf(){
        SparkConf sparkConf = new SparkConf()
                                    .setAppName(sparkConfigLoader.getAppName())
                                    .set(sparkConfigLoader.getCassandra().getHost(), sparkConfigLoader.getLocalhost())
                                    .setMaster(sparkConfigLoader.getMaster())
                                        .set(sparkConfigLoader.getDriver().getName(), sparkConfigLoader.getDriver().getMemory())
                                        .set(sparkConfigLoader.getWorker().getName(), sparkConfigLoader.getWorker().getMemory())
                                        .set(sparkConfigLoader.getExecutor().getName(), sparkConfigLoader.getExecutor().getMemory())
                                        .set(sparkConfigLoader.getRpc().getMessage().getName(), sparkConfigLoader.getRpc().getMessage().getMaxSize());
        return sparkConf;
    }

    @Bean
    @ConditionalOnMissingBean(JavaSparkContext.class)
    public JavaSparkContext javaSparkContext(@Autowired SparkConf sparkConf){
        return new JavaSparkContext(sparkConf);
    }

}
