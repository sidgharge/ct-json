package com.homeprojects.ct.ctjson.core.deserializer;

import com.homeprojects.ct.ctjson.GenericTypeMetadata;
import com.homeprojects.ct.ctjson.core.RuntimeGenericTypeMetadata;

import java.util.HashMap;
import java.util.Map;

public abstract class GenericTypesManager {

    private final Map<String, RuntimeGenericTypeMetadata> map;

    public GenericTypesManager() {
        this.map = new HashMap<>();
    }

    public void add(String key, RuntimeGenericTypeMetadata metadata) {
        map.put(key, metadata);
    }

    public RuntimeGenericTypeMetadata getMetadata(String key) {
        return map.get(key);
    }
}
