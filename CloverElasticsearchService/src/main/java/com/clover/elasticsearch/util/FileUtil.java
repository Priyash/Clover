package com.clover.elasticsearch.util;

import com.google.gson.stream.JsonReader;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.*;

@Component
public class FileUtil {

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private FileUtil fileUtil;

    public File getFile(String fileName) {
        File file = null;
        try {
            Resource resource = resourceLoader.getResource("classpath:" + fileName);
            file = resource.getFile();
            return file;
        } catch (FileNotFoundException fex){
            fex.printStackTrace();
        } catch (IOException iex) {
            iex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return file;
    }

    public JsonReader getJsonReader() throws FileNotFoundException {
        JsonReader jsonReader = new JsonReader(new FileReader(fileUtil.getFile(Constants.ELASTICSEARCH_MAPPING_JSON_FILE_NAME)));
        return jsonReader;
    }

    
}
