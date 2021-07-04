package com.clover.storage.config;

import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class HttpClientConfig {

    @Autowired
    private SparkConfigLoader sparkConfigLoader;

    @Bean
    public PoolingHttpClientConnectionManager poolingHttpClientConnectionManager(){
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setDefaultMaxPerRoute(sparkConfigLoader.getConstants().getConnectionPool().getMaxRouteConnections());
        connectionManager.setMaxTotal(sparkConfigLoader.getConstants().getConnectionPool().getMaxTotalConnections());
        HttpHost localHost = new HttpHost(sparkConfigLoader.getLocalhost(), sparkConfigLoader.getPort());
        connectionManager.setMaxPerRoute(new HttpRoute(localHost), sparkConfigLoader.getConstants().getConnectionPool().getMaxLocalHostConnections());
        return connectionManager;
    }

    @Bean
    public ConnectionKeepAliveStrategy connectionKeepAliveStrategy() {
        return (httpResponse, httpContext) -> {
            HeaderIterator headerIterator = httpResponse.headerIterator(HTTP.CONN_KEEP_ALIVE);
            HeaderElementIterator elementIterator = new BasicHeaderElementIterator(headerIterator);

            while (elementIterator.hasNext()) {
                HeaderElement element = elementIterator.nextElement();
                String param = element.getName();
                String value = element.getValue();
                if (value != null && param.equalsIgnoreCase("timeout")) {
                    return Long.parseLong(value) * 1000; // convert to ms
                }
            }
            return sparkConfigLoader.getConstants().getConnectionPool().getDefaultKeepAliveTime();
        };
    }

    @Bean
    public CloseableHttpClient httpClient(){
        RequestConfig requestConfig = RequestConfig.custom()
                                        .setConnectionRequestTimeout(sparkConfigLoader.getConstants().getTimeouts().getConnectionRequestTimeout())
                                        .setConnectTimeout(sparkConfigLoader.getConstants().getTimeouts().getConnectionTimeout())
                                        .setSocketTimeout(sparkConfigLoader.getConstants().getTimeouts().getSocketTimeout())
                                        .build();
        return HttpClients.custom()
                    .setDefaultRequestConfig(requestConfig)
                    .setConnectionManager(poolingHttpClientConnectionManager())
                    .setKeepAliveStrategy(connectionKeepAliveStrategy())
                    .build();
    }
}
