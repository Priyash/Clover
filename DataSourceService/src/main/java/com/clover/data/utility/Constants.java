package com.clover.data.utility;

public class Constants {
    public static final String DATE_FORMAT_8601_ISO_PATTERN = "yyyy-MM-dd'T'HH:mm:ssZ";
    public static final String TIMEZONE_ID = "UTC";
    public static final String ENTITY_NAME = "entity";
    public static final String STATUS_CODE = "statusCode";
    public static final String ID = "id";
    public static final String STATUS = "status";

    public static final String ACTIVE = "ACTIVE";
    public static final String INACTIVE = "INACTIVE";
    public static final String DRAFT = "DRAFT";
    public static final String MONGO_TBL = "PRODUCT_TBL_TEST";


    public Constants(){
        throw new RuntimeException("This is a Constant class");
    }
}
