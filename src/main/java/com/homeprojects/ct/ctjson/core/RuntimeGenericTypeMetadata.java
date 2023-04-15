package com.homeprojects.ct.ctjson.core;

import java.util.Arrays;
import java.util.List;

public class RuntimeGenericTypeMetadata {

    private final Class<?> clazz;

    private final String genericName;

    private final List<RuntimeGenericTypeMetadata> genericsList;

    public RuntimeGenericTypeMetadata(Class<?> clazz, String genericName, RuntimeGenericTypeMetadata... generics) {
        this.clazz = clazz;
        this.genericName = genericName;
        this.genericsList = Arrays.asList(generics);
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public String getGenericName() {
        return genericName;
    }

    public RuntimeGenericTypeMetadata getGeneric(int index) {
        return genericsList.get(index);
    }

    @Override
    public String toString() {
        return "RuntimeGenericTypeMetadata{" +
                "clazz=" + clazz +
                ", genericName='" + genericName + '\'' +
                ", genericsList=" + genericsList +
                '}';
    }
}
