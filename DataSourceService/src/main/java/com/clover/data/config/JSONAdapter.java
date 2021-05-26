package com.clover.data.config;

import com.clover.data.utility.Constants;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.util.List;

public class JSONAdapter implements DataSourceAdapter {
    static {
        DataSourceConfigFactory.getInstance().registerDataSourceAdapter(Constants.JSON_ADAPTER_ID, JSONAdapter.class.getName());
    }

    @Override
    public List<Object> parse(String fileName) {
        try {
            if(!ObjectUtils.isEmpty(fileName)){
                List<Object> jsonValues = loadJSONData(fileName);
                return jsonValues;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    List<Object> loadJSONData(String fileName){
        try {
            ObjectMapper mapper = new ObjectMapper();
            File file = new ClassPathResource(fileName).getFile();
            List<Object> jsonValues = mapper.readValue(file, new TypeReference<List<Object>>(){});
            return jsonValues;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }


}
