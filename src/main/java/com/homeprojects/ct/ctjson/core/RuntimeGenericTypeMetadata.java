package com.homeprojects.ct.ctjson.core;

import java.util.HashMap;
import java.util.Map;

public class RuntimeGenericTypeMetadata {

    private final Class<?> clazz;

    private final String genericName;

    private final Map<String, RuntimeGenericTypeMetadata> generics;

    public RuntimeGenericTypeMetadata(Class<?> clazz, String genericName, RuntimeGenericTypeMetadata... generics) {
        this.clazz = clazz;
        this.genericName = genericName;
        this.generics = new HashMap<>();
        for (RuntimeGenericTypeMetadata generic : generics) {
            this.generics.put(generic.getGenericName(),generic);
        }
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public String getGenericName() {
        return genericName;
    }

    public RuntimeGenericTypeMetadata getGeneric(String genericName) {
        return generics.get(genericName);
    }

    @Override
    public String toString() {
        return "RuntimeGenericTypeMetadata{" +
                "clazz='" + clazz + '\'' +
                ", genericName='" + genericName + '\'' +
                ", generics=" + generics +
                '}';
    }
}
