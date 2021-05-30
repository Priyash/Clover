package com.clover.spark.Config;

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

//    @Value("${spark.master}")
//    private String sparkMaster;
//
//    @Value("${spark.stream.kafka.durations}")
//    private String streamDurationTime;
//
//    @Value("${spark.driver.memory}")
//    private String sparkDriverMemory;
//
//    @Value("${spark.worker.memory}")
//    private String sparkWorkerMemory;
//
//    @Value("${spark.executor.memory}")
//    private String sparkExecutorMemory;
//
//    @Value("${spark.rpc.message.maxSize}")
//    private String sparkRpcMessageMaxSize;

    @Autowired
    private SparkConfigLoader sparkConfigLoader;

    @Bean
    @ConditionalOnMissingBean(SparkConf.class)
    public SparkConf sparkConf(){
        SparkConf sparkConf = new SparkConf()
                                    .setAppName(this.sparkAppName)
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
