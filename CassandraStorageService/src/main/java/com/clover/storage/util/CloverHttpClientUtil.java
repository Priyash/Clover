package com.clover.storage.util;

import com.clover.storage.config.SparkConfigLoader;
import com.clover.storage.model.Product;
import com.google.gson.Gson;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Component
public class CloverHttpClientUtil {

    @Autowired
    private CloseableHttpClient closeableHttpClient;

    @Autowired
    private Gson gson;

    @Autowired
    private SparkConfigLoader sparkConfigLoader;

    @Async("asyncTaskExecutor")
    public CompletableFuture<Map<String, Object>> postHttpClient(String URI, Object payload) throws IOException {
        Map<String, Object> httpMap = new HashMap<>();
        try {
            if(!StringUtils.isEmpty(URI) && (payload instanceof List)){
                HttpPost httpPostRequest = new HttpPost(URI);
                httpPostRequest.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
                httpPostRequest.addHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
                httpPostRequest.addHeader(HttpHeaders.CONNECTION, "keep-alive");

                String jsonProduct = gson.toJson(payload);
                StringEntity stringEntity = new StringEntity(jsonProduct);

                httpPostRequest.setEntity(stringEntity);
                CloseableHttpResponse httpResponse = closeableHttpClient.execute(httpPostRequest);

                System.out.println("Response Status : " + httpResponse.getStatusLine().getStatusCode());
                HttpEntity entity = httpResponse.getEntity();
                try {
                    if(!ObjectUtils.isEmpty(entity)){
                        String result = EntityUtils.toString(entity);
                        httpMap.put(sparkConfigLoader.getHttpEntityName(), result);
                        httpMap.put(sparkConfigLoader.getStatusCodeName(), httpResponse.getStatusLine().getStatusCode());
                        return CompletableFuture.completedFuture(httpMap);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    httpResponse.close();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CompletableFuture.completedFuture(httpMap);
    }
}
