package com.clover.data.utility;

import com.clover.data.config.DataSourceConfigLoader;
import com.clover.data.model.Product;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
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
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
public class CloverHttpClientUtil {

    @Autowired
    private CloseableHttpClient closeableHttpClient;

    @Autowired
    private Gson gson;

    @Autowired
    private DataSourceConfigLoader configLoader;

    @Async("asyncTaskExecutor")
    public CompletableFuture<Map<String, Object>> postHttpClient(String URI, Object payload) throws IOException {
        log.info("Post http client call of URI: {}, payload: {}", URI, payload.toString());
        Map<String, Object> httpMap = new HashMap<>();
        try {
            if(!StringUtils.isEmpty(URI) && (payload instanceof Product)){
                HttpPost httpPostRequest = new HttpPost(URI);
                httpPostRequest.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
                httpPostRequest.addHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
                httpPostRequest.addHeader(HttpHeaders.CONNECTION, "keep-alive");

                String jsonProduct = gson.toJson(payload);
                StringEntity stringEntity = new StringEntity(jsonProduct);

                httpPostRequest.setEntity(stringEntity);
                CloseableHttpResponse httpResponse = closeableHttpClient.execute(httpPostRequest);
                int responseStatusCode = httpResponse.getStatusLine().getStatusCode();
                System.out.println("Response Status : " + responseStatusCode);
                HttpEntity entity = httpResponse.getEntity();
                log.info("Post http client call response status: {}, entity: {]", responseStatusCode, entity.getContent().toString());
                try {
                    if(!ObjectUtils.isEmpty(entity)){
                        String result = EntityUtils.toString(entity);
                        httpMap.put(Constants.ENTITY_NAME, result);
                        httpMap.put(Constants.STATUS_CODE, responseStatusCode);
                        return CompletableFuture.completedFuture(httpMap);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    httpResponse.close();
                }
            }
        } catch (Exception ex) {
            log.error("Exception while invoking http post call of URI: {}", URI, ex);
        }
        return CompletableFuture.completedFuture(httpMap);
    }
}
