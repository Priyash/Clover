package com.clover.elasticsearch.config;

import lombok.Data;


@Data
public class MappingsConfig {
    private MappingsConfig.Mappings mappings = new MappingsConfig.Mappings();

    @Data
    public static class Mappings {
        private Mappings.Properties properties = new Mappings.Properties();

        @Data
        public static class Properties {
            private Properties.a a = new Properties.a();
            private Properties.b b = new Properties.b();

            @Data
            public static class a {
                private String key;
                private String type;
            }

            @Data
            public static class b {
                private String key;
                private String type;
            }
        }
    }

}
