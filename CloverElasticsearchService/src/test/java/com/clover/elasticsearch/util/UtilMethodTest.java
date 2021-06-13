package com.clover.elasticsearch.util;

import com.clover.elasticsearch.config.MappingsConfig;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileNotFoundException;
import java.io.FileReader;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {com.clover.elasticsearch.config.ElasticSearchYMLConfig.class, com.clover.elasticsearch.util.FileUtil.class, com.google.gson.Gson.class})
public class UtilMethodTest {

    @Autowired
    private FileUtil fileUtil;

    @Autowired
    private Gson gson;

    @Test
    public void test_CreateIndexMap() throws FileNotFoundException {
        JsonReader jsonReader = new JsonReader(new FileReader(fileUtil.getFile(Constants.ELASTICSEARCH_MAPPING_JSON_FILE_NAME)));
        MappingsConfig mapper = gson.fromJson(jsonReader, MappingsConfig.class);
        mapper.getMappings().getProperties().getA();
        Assert.assertEquals(mapper.getMappings().getProperties().getA().getType(), "integer");
        Assert.assertEquals(mapper.getMappings().getProperties().getB().getType(), "text");
    }
}
