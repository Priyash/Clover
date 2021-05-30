package com.clover.spark.client;

import com.clover.spark.Model.Product;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

@Component
public class CloverHttpClient {

    @Autowired
    private CloseableHttpClient closeableHttpClient;

    @Async("asyncTaskExecutor")
    public CompletableFuture<String> postHttpClient(String URI){
        try {
            if(!StringUtils.isEmpty(URI)){
                HttpPost httpPostRequest = new HttpPost(URI);
                httpPostRequest.addHeader(HttpHeaders.CONTENT_TYPE, "application/json");
                httpPostRequest.addHeader(HttpHeaders.CONNECTION, "keep-alive");
                CloseableHttpResponse httpResponse = closeableHttpClient.execute(httpPostRequest);
                HttpEntity entity = httpResponse.getEntity();
                try {
                    if(!ObjectUtils.isEmpty(entity)){
                        String result = EntityUtils.toString(entity);
                        return CompletableFuture.completedFuture(result);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    httpResponse.close();
                    closeableHttpClient.close();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CompletableFuture.completedFuture(null);
    }
}
