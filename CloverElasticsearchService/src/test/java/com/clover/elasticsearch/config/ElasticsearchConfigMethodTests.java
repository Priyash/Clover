package com.clover.elasticsearch.config;

import com.clover.elasticsearch.util.IndexMapUtil;
import com.google.gson.Gson;
import org.apache.http.HttpHost;
import org.elasticsearch.action.support.ActiveShardCount;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.common.unit.TimeValue;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.FileNotFoundException;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {com.clover.elasticsearch.config.AppConfig.class,
                            IndexMapUtil.class,
                            com.clover.elasticsearch.util.FileUtil.class,
                            com.google.gson.Gson.class,
                            com.clover.elasticsearch.config.MappingsConfig.class,
                            org.springframework.core.io.ResourceLoader.class
                            })
public class ElasticsearchConfigMethodTests {

    @Autowired
    private ElasticSearchYMLConfig ymlElasticSearchConfig;

    @Autowired
    private IndexMapUtil indexMapUtil;

    @Autowired
    private Gson gson;

    @Test
    public void createHighLevelRestClientTest() {
        String host = ymlElasticSearchConfig.getConfig().getLocalhost();
        Integer port = ymlElasticSearchConfig.getConfig().getPort();
        String httpMode = ymlElasticSearchConfig.getConfig().getMode();


        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(new HttpHost(host,
                                                port,
                                                httpMode))
        );

        Assert.assertNotNull(client);
    }

    @Test
    public void createIndexMap_Test() throws FileNotFoundException {
        CreateIndexRequest request = new CreateIndexRequest(ymlElasticSearchConfig.getIndex().getIndexName());
        request.settings(indexMapUtil.createSettings());
        Map<String, Object> mapping = indexMapUtil.createIndexMap();
        request.mapping(mapping);
        request.setTimeout(TimeValue.timeValueMinutes(2));
        request.setMasterTimeout(TimeValue.timeValueMinutes(1));
        request.waitForActiveShards(ActiveShardCount.DEFAULT);

        Assert.assertNotNull(request);
        Assert.assertEquals(mapping.size(), 1);
    }

}
