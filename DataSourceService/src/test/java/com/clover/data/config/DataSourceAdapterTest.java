package com.clover.data.config;

import com.clover.data.model.Product;
import com.clover.data.utility.Constants;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.List;
import java.util.Map;


@RunWith(MockitoJUnitRunner.class)
public class DataSourceAdapterTest {
    @Before
    public void setup(){
        try {
            Class.forName(Constants.JSON_ADAPTER_CLASS);
            Class.forName(Constants.CSV_ADAPTER_CLASS);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void Test_CSVDataSourceMethod(){
        String fileName = "test_data.csv";
        CSVAdapter csvAdapter = new CSVAdapter();

        Product product1 = new Product.ProductBuilder()
                            .a(1)
                            .b("Eldon Base for stackable storage shelf")
                            .build();

        List<Object> testValues = csvAdapter.parse(fileName);
        Map<String, Object> productMap = (Map<String, Object>) testValues.get(0);
        Product product2 = new Product.ProductBuilder()
                                .a(Integer.valueOf((String)productMap.get("a")))
                                .b((String)productMap.get("b"))
                                .build();

        Assert.assertEquals(product1.getA(),product2.getA());
        Assert.assertEquals(product1.getB(),product2.getB());
    }


    @Test
    public void Test_JSONDataSourceMethod(){
        String fileName = "test_json_data.json";
        JSONAdapter jsonAdapter = new JSONAdapter();

        Product product1 = new Product.ProductBuilder()
                .a(1)
                .b("Eldon Base for stackable storage shelf")
                .build();

        List<Object> testValues = jsonAdapter.parse(fileName);
        Map<String, Object> productMap = (Map<String, Object>) testValues.get(0);
        Product product2 = new Product.ProductBuilder()
                .a((Integer) productMap.get("a"))
                .b((String)productMap.get("b"))
                .build();

        Assert.assertEquals(product1.getA(),product2.getA());
        Assert.assertEquals(product1.getB(),product2.getB());
    }

    @Test
    public void Test_DataSource_JSON_ConfigFactoryMethod(){
        DataSourceAdapter dataSourceAdapter = DataSourceConfigFactory.getInstance().getDataSourceAdapter(Constants.JSON_ADAPTER_ID);
        Product product1 = new Product.ProductBuilder()
                .a(1)
                .b("Eldon Base for stackable storage shelf")
                .build();

        String fileName = "test_json_data.json";
        List<Object> testValues = dataSourceAdapter.parse(fileName);
        Map<String, Object> productMap = (Map<String, Object>) testValues.get(0);
        Product product2 = new Product.ProductBuilder()
                .a((Integer) productMap.get("a"))
                .b((String)productMap.get("b"))
                .build();

        Assert.assertEquals(product1.getA(),product2.getA());
        Assert.assertEquals(product1.getB(),product2.getB());
    }

    @Test
    public void Test_DataSource_CSV_ConfigFactoryMethod(){
        DataSourceAdapter dataSourceAdapter = DataSourceConfigFactory.getInstance().getDataSourceAdapter(Constants.CSV_ADAPTER_ID);
        Product product1 = new Product.ProductBuilder()
                .a(1)
                .b("Eldon Base for stackable storage shelf")
                .build();

        String fileName = "test_data.csv";
        List<Object> testValues = dataSourceAdapter.parse(fileName);
        Map<String, Object> productMap = (Map<String, Object>) testValues.get(0);
        Product product2 = new Product.ProductBuilder()
                .a(Integer.valueOf((String)productMap.get("a")))
                .b((String)productMap.get("b"))
                .build();

        Assert.assertEquals(product1.getA(),product2.getA());
        Assert.assertEquals(product1.getB(),product2.getB());
    }
}
