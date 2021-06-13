package com.clover.elasticsearch.config;

import com.clover.elasticsearch.util.IndexMapUtil;
import org.apache.http.HttpHost;
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
public class ElasticSearchConfig  {
    @Autowired
    private ElasticSearchYMLConfig ymlElasticSearchConfig;

    @Autowired
    private IndexMapUtil indexMapUtil;

    private Boolean isSuccess = false;

    @Bean
    //Reference_1 : https://medium.com/@ashish_fagna/getting-started-with-elasticsearch-creating-indices-inserting-values-and-retrieving-data-e3122e9b12c6
    public RestHighLevelClient restHighLevelClient(){
        String host = ymlElasticSearchConfig.getConfig().getLocalhost();
        Integer port = ymlElasticSearchConfig.getConfig().getPort();
        String httpMode = ymlElasticSearchConfig.getConfig().getMode();
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(new HttpHost(host,
                                                port,
                                                httpMode)));
        return client;
    }

    @Bean
    public CreateIndexRequest createIndexRequest() {
        CreateIndexRequest request = null;
        try {
            request = new CreateIndexRequest(ymlElasticSearchConfig.getIndex().getIndexName());
            request.settings(indexMapUtil.createSettings());
            Map<String, Object> mapping = indexMapUtil.createIndexMap();
            request.mapping(mapping);
            request.setTimeout(TimeValue.timeValueMinutes(2));
            request.setMasterTimeout(TimeValue.timeValueMinutes(1));
            request.waitForActiveShards(ActiveShardCount.DEFAULT);
            return request;
        } catch (IndexCreationException iex) {
            iex.fillInStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return request;
    }


    @Bean
    public Boolean createAsyncIndex() {
        CreateIndexResponse response = null;
        try {
            long startTime = System.currentTimeMillis();
            PlainActionFuture<CreateIndexResponse> createIndexResponseFuture = new PlainActionFuture<>();
            Boolean isExist = indexMapUtil.isIndexExist(ymlElasticSearchConfig.getIndex().getIndexName(), restHighLevelClient());
            if(isExist){
                indexMapUtil.deleteIndex(ymlElasticSearchConfig.getIndex().getIndexName(), restHighLevelClient());
                isExist = false;
            }
            if(!isExist) {
                restHighLevelClient().indices().createAsync(createIndexRequest(), RequestOptions.DEFAULT, createIndexResponseFuture);
                Boolean isTimeoutOrCompleted = indexMapUtil.isCompleted(createIndexResponseFuture, startTime, "Timeout for creating elasticsearch index has happened during asynchronous call ");
                if(isTimeoutOrCompleted) {
                    response = createIndexResponseFuture.actionGet(TimeValue.timeValueMillis(100));
                    return response.isAcknowledged() && response.isShardsAcknowledged();
                }
            }

        } catch (CompletionException cex) {
            cex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
