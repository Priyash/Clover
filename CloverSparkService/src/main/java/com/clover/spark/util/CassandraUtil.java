package com.clover.spark.util;

import com.clover.spark.Config.SparkConfigLoader;
import com.clover.spark.Constants.Constants;
import com.clover.spark.Model.Product;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;

@Component
public class CassandraUtil {
    @Autowired
    private CloverHttpClientUtil cloverHttpClientUtil;

    @Autowired
    private SparkConfigLoader sparkConfigLoader;

    public Map<String, Object> saveToCassandra(Product product) throws ExecutionException, InterruptedException, IOException {
        Map<String, Object> cassandra_save_result_status = new HashMap<>();
        CompletableFuture<Map<String, Object>> cassandraInsertFuture = null;
        try {
            cassandraInsertFuture = cloverHttpClientUtil.postHttpClient(Constants.CASSANDRA_STREAM_INSERT_URI, product);
            Boolean isCompletedOrTimedOut = false;
            long timeout = sparkConfigLoader.getConstants().getTimeouts().getCompletableFutureTimeoutValue();
            long startTime = System.currentTimeMillis();

            while(!isCompletedOrTimedOut) {
                if(cassandraInsertFuture.isDone() && !cassandraInsertFuture.isCancelled()){
                    isCompletedOrTimedOut = true;
                }
                long currentTime = System.currentTimeMillis();
                //IF the task taking more than 10s
                if((currentTime - startTime) > timeout){
                    isCompletedOrTimedOut = true;
                }
            }

            if(!ObjectUtils.isEmpty(cassandraInsertFuture)){
                cassandra_save_result_status = cassandraInsertFuture.get();
            }
        } catch (CompletionException cex) {
            if(cassandraInsertFuture.isCompletedExceptionally()){
                cassandraInsertFuture.completeExceptionally(cex);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return cassandra_save_result_status;
    }
}
