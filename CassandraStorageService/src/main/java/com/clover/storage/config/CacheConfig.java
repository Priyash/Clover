package com.clover.storage.config;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfig {

    @Bean
    public LoadingCache<String, Object> buildCache() {
        LoadingCache<String, Object> cache = CacheBuilder.newBuilder()
                                            .maximumSize(100)
                                            .expireAfterWrite(1000, TimeUnit.MILLISECONDS)
                                            .build(new CacheLoader<String, Object>() {

                                                @Override
                                                public Object load(String s) throws Exception {
                                                    return null;
                                                }
                                            });
        return cache;
    }

}
