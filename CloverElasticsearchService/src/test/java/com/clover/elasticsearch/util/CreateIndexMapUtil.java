package com.clover.elasticsearch.util;

import com.clover.elasticsearch.config.ElasticSearchYMLConfig;
import com.clover.elasticsearch.config.MappingsConfig;
import com.google.gson.Gson;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.action.support.PlainActionFuture;
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
public class CreateIndexMapUtil {

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
        PlainActionFuture<Boolean> booleanFuture = new PlainActionFuture<>();
        try {
            indexRequest = new GetIndexRequest(index);
            indexRequest.local(false);
            indexRequest.humanReadable(true);
            indexRequest.includeDefaults(false);
            indexRequest.indicesOptions(IndicesOptions.LENIENT_EXPAND_OPEN);
            client.indices().existsAsync(indexRequest, RequestOptions.DEFAULT, booleanFuture);
            if(booleanFuture.isDone() && !booleanFuture.isCancelled()) {
                Boolean isExist = booleanFuture.actionGet(TimeValue.timeValueMinutes(1));
                return isExist;
            } else {
                throw new CompletionException(new Throwable("Error checking index " + index ));
            }
        } catch (CompletionException cex) {
            cex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
