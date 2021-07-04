package com.clover.data.utility;




public class Constants {
    public static final String DATE_FORMAT_8601_ISO_PATTERN = "yyyy-MM-dd'T'HH:mm:ssZ";
    public static final String TIMEZONE_ID = "UTC";

    public static final String PRODUCT_TITLE_KEY = "title";
    public static final String PRODUCT_TITLE_DEFAULT_KEY = "default_title";


    public static final String PRODUCT_DESCRIPTION_KEY = "description";
    public static final String PRODUCT_DESCRIPTION_DEFAULT_KEY = "default_description";

    public static final String PRODUCT_VENDOR_KEY = "vendor";
    public static final String PRODUCT_VENDOR_DEFAULT_KEY = "default_vendor";

    public static final String PRODUCT_OPTIONS_KEY = "options";
    public static final String PRODUCT_VARIANTS_KEY = "variants";
    public static final String PRODUCT_IMAGES_KEY = "images";

    public static final String PRODUCT_VARIANT_PRICE_KEY = "price";
    public static final Double PRODUCT_VARIANT_DEFAULT_PRICE_VALUE = 0.0;


    public static final String PRODUCT_VARIANT_OPTION1_KEY = "option1";
    public static final String PRODUCT_VARIANT_OPTION2_KEY = "option2";
    public static final String PRODUCT_VARIANT_OPTION3_KEY = "option3";
    public static final String PRODUCT_VARIANT_TITLE_KEY = "option3";


    public static final String PRODUCT_ID_KEY = "product_id";
    public static final Long PRODUCT_ID_DEFAULT_VALUE = 0L;


    public Constants(){
        throw new RuntimeException("This is a Constant class");
    }
}
