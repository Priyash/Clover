package com.clover.data.utility;

import com.clover.data.config.DataSourceConfigLoader;
import com.clover.data.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
public class KafkaClientUtil {

    @Autowired
    private CloverHttpClientUtil cloverHttpClientUtil;

    @Autowired
    private DataSourceConfigLoader configLoader;

    public Map<String, Object> sendToKafkaStream(Product product) {
        Map<String, Object> kafkaResults = new HashMap<>();
        CompletableFuture<Map<String, Object>> kafkaFutureResult = null;
        Boolean isCompletedOrTimedOut = false;
        Long startTime = System.currentTimeMillis();
        long timeout = configLoader.getConstants().getTimeouts().getCompletableFutureTimeoutValue();
        try {
            kafkaFutureResult = cloverHttpClientUtil.postHttpClient(configLoader.getKafkaConfig().getUrl(), product);
            while (!isCompletedOrTimedOut) {
                Long currTime = System.currentTimeMillis();
                if(kafkaFutureResult.isDone() || !kafkaFutureResult.isCancelled()) {
                    isCompletedOrTimedOut = true;
                }
                if((currTime - startTime) > timeout) {
                    isCompletedOrTimedOut = true;
                    log.info("Timed-Out happened while getting the future result of kafka post http call id: {}", product.getId());
                }
            }
            kafkaResults = kafkaFutureResult.get();
            return kafkaResults;
        } catch (Exception ex) {
            log.error("Exception while executing the kafka url in sendToKafkaStream ", ex);
        }
        return kafkaResults;
    }
}
