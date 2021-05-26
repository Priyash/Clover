package com.clover.data.config;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

public class DataSourceConfigFactory {
    private static DataSourceConfigFactory instance = null;
    private static Map<String, String> registeredDataSourceAdapters = new HashMap<>();
    private DataSourceConfigFactory(){}

    public static DataSourceConfigFactory getInstance() {
        if(instance == null) {
            synchronized(DataSourceConfigFactory.class) {
                if(instance == null){
                    return new DataSourceConfigFactory();
                }
            }
        }
        return instance;
    }

    public void registerDataSourceAdapter(String dataSourceAdapterID, String className){
        try{
            registeredDataSourceAdapters.put(dataSourceAdapterID, className);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //Implemented this
    //https://www.oodesign.com/factory-pattern.html
    public DataSourceAdapter getDataSourceAdapter(String dataSourceAdapterID){
        try {
            String adapterClass = (String)registeredDataSourceAdapters.get(dataSourceAdapterID);
            if(adapterClass != null){
                Constructor adaptorConstructor = Class.forName(adapterClass).getDeclaredConstructor();
                return (DataSourceAdapter) adaptorConstructor.newInstance(new Object[]{ });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
