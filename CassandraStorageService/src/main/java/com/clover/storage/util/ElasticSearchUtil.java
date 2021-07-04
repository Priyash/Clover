package com.clover.storage.util;

import com.clover.storage.config.SparkConfigLoader;
import com.clover.storage.model.Product;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;

@Component
public class ElasticSearchUtil {

    @Autowired
    private CloverHttpClientUtil cloverHttpClientUtil;

    @Autowired
    private SparkConfigLoader sparkConfigLoader;

    public Map<String, Object> saveToElasticSearch(List<Product> products) throws ExecutionException, InterruptedException, IOException {
        Map<String, Object> elasticSearchSaveResults = new HashMap<>();
        CompletableFuture<Map<String, Object>> elasticSearchIndexingFuture = null;
        try {
            elasticSearchIndexingFuture = cloverHttpClientUtil.postHttpClient(sparkConfigLoader.getCassandra().getStreamElasticSearchURI(), products);
            Boolean isCompletedOrTimedOut = false;
            long timeout = sparkConfigLoader.getConstants().getTimeouts().getCompletableFutureTimeoutValue();
            long startTime = System.currentTimeMillis();

            while(!isCompletedOrTimedOut) {
                if(elasticSearchIndexingFuture.isDone() && !elasticSearchIndexingFuture.isCancelled()){
                    isCompletedOrTimedOut = true;
                }
                long currentTime = System.currentTimeMillis();
                //IF the task taking more than 10s
                if((currentTime - startTime) > timeout){
                    isCompletedOrTimedOut = true;
                }
            }

            if(!ObjectUtils.isEmpty(elasticSearchIndexingFuture)){
                elasticSearchSaveResults = elasticSearchIndexingFuture.get();
            }
        } catch (CompletionException cex) {
            if(elasticSearchIndexingFuture.isCompletedExceptionally()){
                elasticSearchIndexingFuture.completeExceptionally(cex);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return elasticSearchSaveResults;
    }
}
