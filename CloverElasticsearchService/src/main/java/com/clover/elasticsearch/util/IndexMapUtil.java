package com.clover.elasticsearch.util;

import com.clover.elasticsearch.config.ElasticSearchYMLConfig;
import com.clover.elasticsearch.config.MappingsConfig;
import com.google.gson.Gson;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.action.support.PlainActionFuture;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletionException;

@Component
public class IndexMapUtil {

    private static IndexMapUtil indexMapUtil = null;

    @Autowired
    private ElasticSearchYMLConfig ymlElasticSearchConfig;

    @Autowired
    private FileUtil fileUtil;

    @Autowired
    private Gson gson;

    public Settings.Builder createSettings() {
        return Settings.builder()
                .put(ymlElasticSearchConfig.getIndex().getIndexNumberOfShardsKey(),
                        ymlElasticSearchConfig.getIndex().getIndexNumberOfShardsValue())
                .put(ymlElasticSearchConfig.getIndex().getIndexNumberOfReplicaKey(),
                        ymlElasticSearchConfig.getIndex().getIndexNumberOfReplicaValue()
                );
    }

    public Map<String, Object> createIndexMap() throws FileNotFoundException {
        //Mapping the elasticsearch json to java pojo class
        MappingsConfig mapper = gson.fromJson(fileUtil.getJsonReader(), MappingsConfig.class);

        //Adding fields type
        Map<String, Object> a = new HashMap<>();
        a.put(Constants.FIELD_NAME_TYPE, mapper.getMappings().getProperties().getA().getType());
        Map<String, Object> b = new HashMap<>();
        b.put(Constants.FIELD_NAME_TYPE, mapper.getMappings().getProperties().getB().getType());

        //Properties field add
        Map<String, Object> properties = new HashMap<>();
        properties.put(mapper.getMappings().getProperties().getA().getKey(), a);
        properties.put(mapper.getMappings().getProperties().getB().getKey(), b);

        //Mapping object creation
        Map<String, Object> mapping = new HashMap<>();
        mapping.put(Constants.FIELD_NAME_PROPERTIES, properties);

        return mapping;
    }

    public Boolean isIndexExist(String index, RestHighLevelClient client) {
        GetIndexRequest indexRequest = null;
        long startTime = System.currentTimeMillis();
        PlainActionFuture<Boolean> booleanFuture = new PlainActionFuture<>();
        try {
            indexRequest = new GetIndexRequest(index);
            indexRequest.local(false);
            indexRequest.humanReadable(true);
            indexRequest.includeDefaults(false);
            indexRequest.indicesOptions(IndicesOptions.LENIENT_EXPAND_OPEN);
            client.indices().existsAsync(indexRequest, RequestOptions.DEFAULT, booleanFuture);
            Boolean isCompleted = isCompleted(booleanFuture, startTime, "Timeout for checking elasticsearch index existence has happened during asynchronous call ");
            if(isCompleted) {
                Boolean isExist = booleanFuture.actionGet(TimeValue.timeValueMillis(100));
                return isExist;
            }
        } catch (CompletionException cex) {
            cex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public Boolean deleteIndex(String index, RestHighLevelClient client) {
        DeleteIndexRequest deleteIndexRequest = null;
        PlainActionFuture<AcknowledgedResponse> acknowledgedResponseFuture = new PlainActionFuture<>();
        long startTime = System.currentTimeMillis();
        try {
            deleteIndexRequest = new DeleteIndexRequest(index);
            deleteIndexRequest.timeout(TimeValue.timeValueMinutes(2));
            deleteIndexRequest.masterNodeTimeout(TimeValue.timeValueMinutes(1));
            deleteIndexRequest.indicesOptions(IndicesOptions.LENIENT_EXPAND_OPEN);
            client.indices().deleteAsync(deleteIndexRequest, RequestOptions.DEFAULT, acknowledgedResponseFuture);
            Boolean isCompleted = isCompleted(acknowledgedResponseFuture, startTime, "Exception deleting the elasticsearch index during asynchronous call");
            if(isCompleted) {
                AcknowledgedResponse response = acknowledgedResponseFuture.actionGet(TimeValue.timeValueMillis(100));
                return response.isAcknowledged();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }


    public  <T> Boolean isCompleted(PlainActionFuture<T> actionFuture, long startTime, String exceptionMessage) {
        Boolean isTimeoutOrCompleted = false;
        while(!isTimeoutOrCompleted) {
            long currentTime = System.currentTimeMillis();
            if(actionFuture.isDone() && !actionFuture.isCancelled()) {
                isTimeoutOrCompleted = true;
                return true;
            }

            if(Math.subtractExact(currentTime, currentTime) > 2000) {
                throw new CompletionException(new Throwable(exceptionMessage));
            }
        }
        return false;
    }
}
