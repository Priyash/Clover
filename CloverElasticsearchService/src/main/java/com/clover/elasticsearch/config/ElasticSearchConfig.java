package com.clover.elasticsearch.config;

import com.clover.elasticsearch.util.IndexMapUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.support.ActiveShardCount;
import org.elasticsearch.action.support.PlainActionFuture;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.indices.IndexCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.concurrent.CompletionException;

@Configuration
@Slf4j
public class ElasticSearchConfig  {
    @Autowired
    private ElasticSearchYMLConfig ymlElasticSearchConfig;

    @Autowired
    private IndexMapUtil indexMapUtil;

    @Bean
    //Reference_1 : https://medium.com/@ashish_fagna/getting-started-with-elasticsearch-creating-indices-inserting-values-and-retrieving-data-e3122e9b12c6
    public RestHighLevelClient restHighLevelClient(){
        log.info("Creating RestHighLevelClient bean...");
        String host = ymlElasticSearchConfig.getConfig().getLocalhost();
        Integer port = ymlElasticSearchConfig.getConfig().getPort();
        String httpMode = ymlElasticSearchConfig.getConfig().getMode();
        log.info("host: {}, port: {}, httpMode; {}", host, port, httpMode);
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(new HttpHost(host,
                                                port,
                                                httpMode)));
        return client;
    }

    @Bean
    public CreateIndexRequest createIndexRequest() {
        log.info("Creating elasticsearch index...");
        CreateIndexRequest request = null;
        try {
            request = new CreateIndexRequest(ymlElasticSearchConfig.getIndex().getIndexName());
            request.settings(indexMapUtil.createSettings());
            Map<String, Object> mapping = indexMapUtil.createIndexMap();
            request.mapping(mapping);
            request.setTimeout(TimeValue.timeValueMinutes(2));
            request.setMasterTimeout(TimeValue.timeValueMinutes(1));
            request.waitForActiveShards(ActiveShardCount.DEFAULT);
            log.info("CreateIndexRequest: {} ", request.toString());
            return request;
        } catch (IndexCreationException iex) {
            iex.fillInStackTrace();
            log.error("IndexCreationException while creating ElasticSearch Index Request: {} ", iex);
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("Exception while creating ElasticSearch Index Request: {} ", ex);
        }
        return request;
    }


    @Bean
    public void createAsyncIndex() {
        log.info("Creating ElasticSearch Index using createAsync method...");
        CreateIndexResponse response = null;
        try {
            Boolean isExist = indexMapUtil.isIndexExist(ymlElasticSearchConfig.getIndex().getIndexName(), restHighLevelClient());
            if(!isExist) {
                restHighLevelClient().indices().createAsync(createIndexRequest(), RequestOptions.DEFAULT, new ActionListener<CreateIndexResponse>() {
                    @Override
                    public void onResponse(CreateIndexResponse createIndexResponse) {
                        log.info("ElasticSearch index: {} has been created successfully", ymlElasticSearchConfig.getIndex().getIndexName());
                        log.info("Index acknowledged: {}", createIndexResponse.isAcknowledged());
                        log.info("Index shards acknowledged: {}", createIndexResponse.isShardsAcknowledged());
                    }

                    @Override
                    public void onFailure(Exception e) {
                        log.error("Failure on creating ElasticSearch index: {} , using createAsyncMethod", ymlElasticSearchConfig.getIndex().getIndexName(), e);
                    }
                });
            }

        } catch (CompletionException cex) {
            cex.printStackTrace();
            log.error("CompletionException on creating ElasticSearch index: {}", ymlElasticSearchConfig.getIndex().getIndexName(), cex);
        } catch (Exception ex) {
            log.error("Exception on creating ElasticSearch index: {}", ymlElasticSearchConfig.getIndex().getIndexName(), ex);
        }
    }
}
