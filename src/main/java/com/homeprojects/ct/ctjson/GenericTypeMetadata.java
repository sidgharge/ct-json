package com.homeprojects.ct.ctjson;

import javax.lang.model.element.TypeElement;
import java.util.ArrayList;
import java.util.List;

public class GenericTypeMetadata {

    private final TypeElement clazz;

    private final String genericName;

    private final List<GenericTypeMetadata> generics;

    public GenericTypeMetadata(TypeElement clazz, String genericName) {
        this.clazz = clazz;
        this.genericName = genericName;
        this.generics = new ArrayList<>();
    }

    public TypeElement getClazz() {
        return clazz;
    }

    public String getGenericName() {
        return genericName;
    }

    public void addGeneric(GenericTypeMetadata generic) {
        generics.add(generic);
    }

    public List<GenericTypeMetadata> getGenerics() {
        return generics;
    }

    @Override
    public String toString() {
        return "GenericTypeMetadata{" +
                "clazz='" + clazz + '\'' +
                ", genericName='" + genericName + '\'' +
                ", generics=" + generics +
                '}';
    }
}
