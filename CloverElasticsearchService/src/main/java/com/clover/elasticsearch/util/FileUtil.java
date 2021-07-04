package com.clover.elasticsearch.util;

import com.google.gson.stream.JsonReader;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.*;

@Component
@Slf4j
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
            log.error("FileNotFoundException while fetching file: {} ", fileName, fex);
        } catch (IOException iex) {
            log.error("IOException while fetching file: {} ", fileName, iex);
        } catch (Exception ex) {
            log.error("Exception while fetching file: {} ", fileName, ex);
        }
        return file;
    }

    public JsonReader getJsonReader() throws FileNotFoundException {
        JsonReader jsonReader = new JsonReader(new FileReader(fileUtil.getFile(Constants.ELASTICSEARCH_MAPPING_JSON_FILE_NAME)));
        return jsonReader;
    }

    
}
