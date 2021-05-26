package com.clover.store.data.config;

import java.util.List;

public interface DataSourceAdapter {
    public List<Object> parse(String fileName);
}
