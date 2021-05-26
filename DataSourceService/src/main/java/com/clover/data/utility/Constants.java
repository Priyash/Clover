package com.clover.data.utility;

public class Constants {

    public static String CSV_ADAPTER_ID = "csv";
    public static String JSON_ADAPTER_ID = "json";

    public static String JSON_ADAPTER_CLASS = "com.clover.store.datastoreservice.config.JSONAdapter";
    public static String CSV_ADAPTER_CLASS = "com.clover.store.datastoreservice.config.CSVAdapter";


    public Constants(){
        throw new RuntimeException("This is a Constant class");
    }
}
