package com.clover.data.builder;

import java.util.Map;

public interface Builder<T, V> {
    T build(Map<String, Object> objectMap);
    T buildDefault(Map<String, Object> objectMap);
    T updateObject(V product, Map<String, Object> objectMap);
}
