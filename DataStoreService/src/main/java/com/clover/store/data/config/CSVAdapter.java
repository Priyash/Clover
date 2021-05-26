package com.clover.store.data.config;

import com.clover.store.data.utility.Constants;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.util.List;
import java.util.Map;

public class CSVAdapter implements DataSourceAdapter {
    static {
        DataSourceConfigFactory.getInstance().registerDataSourceAdapter(Constants.CSV_ADAPTER_ID, CSVAdapter.class.getName());
    }

    @Override
    public List<Object> parse(String fileName) {
        try {
            if(!fileName.isEmpty() || !fileName.isBlank()){
                List<Object> productValues = loadCSVData(Map.class, fileName);
                return productValues;
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    private <T> List<T> loadCSVData(Class type, String fileName){
        try {
            //CsvSchema schema = CsvSchema.builder().addColumn("parentCategoryCode").addColumn("code").addColumn("name").addColumn("description").build();
            CsvSchema bootstrapSchema = CsvSchema.builder().setUseHeader(true).build();
            CsvMapper mapper = new CsvMapper();
            File file = new ClassPathResource(fileName).getFile();
            List<T> csvValues =
                    (List<T>) mapper.readerFor(Map.class).with(bootstrapSchema).readValues(file).readAll();
            return csvValues;
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
}
