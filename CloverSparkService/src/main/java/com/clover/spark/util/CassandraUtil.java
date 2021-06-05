package com.clover.spark.util;

import com.clover.spark.Constants.Constants;
import com.clover.spark.Model.Product;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Component
public class CassandraUtil {
    @Autowired
    private CloverHttpClientUtil cloverHttpClientUtil;

    public String saveToCassandra(Product product) throws ExecutionException, InterruptedException, IOException {
        String cassandra_save_result_status = "";
        CompletableFuture<String> cassandraInsertFuture = cloverHttpClientUtil.postHttpClient(Constants.CASSANDRA_STREAM_INSERT_URI, product);
        Boolean isCompleted = false;
        while(!isCompleted) {
            if(cassandraInsertFuture.isDone() && !cassandraInsertFuture.isCancelled()){
                isCompleted = true;
            }
        }

        if(!ObjectUtils.isEmpty(cassandraInsertFuture)){
            cassandra_save_result_status = cassandraInsertFuture.get();
        }

        return cassandra_save_result_status;
    }
}
