package com.clover.store.spark.CloverSparkService.Config;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SparkConfig {

    @Value("${spring.application.name}")
    private String sparkAppName;

    @Value("${spark.master}")
    private String sparkMaster;

    @Value("${spark.stream.kafka.durations}")
    private String streamDurationTime;

    @Value("${spark.driver.memory}")
    private String sparkDriverMemory;

    @Value("${spark.worker.memory}")
    private String sparkWorkerMemory;

    @Value("${spark.executor.memory}")
    private String sparkExecutorMemory;

    @Value("${spark.rpc.message.maxSize}")
    private String sparkRpcMessageMaxSize;

    @Bean
    @ConditionalOnMissingBean(SparkConf.class)
    public SparkConf sparkConf(){
        SparkConf sparkConf = new SparkConf()
                                    .setAppName(this.sparkAppName)
                                    .set("spark.cassandra.connection.host", "127.0.0.1")
                                    .setMaster("local[*]")
                                        .set("spark.driver.memory", this.sparkDriverMemory)
                                        .set("spark.worker.memory", this.sparkWorkerMemory)
                                        .set("spark.executor.memory", this.sparkExecutorMemory)
                                        .set("spark.rpc.message.maxSize", this.sparkRpcMessageMaxSize);
        return sparkConf;
    }

    @Bean
    @ConditionalOnMissingBean(JavaSparkContext.class)
    public JavaSparkContext javaSparkContext(@Autowired SparkConf sparkConf){
        return new JavaSparkContext(sparkConf);
    }

}
