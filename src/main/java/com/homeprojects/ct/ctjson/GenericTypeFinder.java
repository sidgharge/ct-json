package com.homeprojects.ct.ctjson;

import com.homeprojects.ct.ctjson.annotations.GenericType;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GenericTypeFinder {

    private final ProcessingEnvironment processingEnvironment;

    private final RoundEnvironment roundEnvironment;

    private final Map<String, GenericTypeMetadata> map;

    public GenericTypeFinder(ProcessingEnvironment processingEnvironment, RoundEnvironment roundEnvironment) {
        this.processingEnvironment = processingEnvironment;
        this.roundEnvironment = roundEnvironment;
        map = new HashMap<>();
    }

    public Map<String, GenericTypeMetadata> find() {
        Set<? extends Element> set = roundEnvironment.getElementsAnnotatedWith(GenericType.class);
        set.forEach(this::process);
        return map;
    }

    private void process(Element e) {
        ExecutableElement ee = (ExecutableElement) e;
        String key = ee.getAnnotation(GenericType.class).key();
        TypeMirror returnType = ee.getReturnType();
        GenericTypeMetadata metadata = processType(((DeclaredType) returnType).getTypeArguments().get(0), "T");
        System.out.println(metadata);
        map.put(key, metadata);
    }
    
    private GenericTypeMetadata processType(TypeMirror type, String genericName) {
        if(!(type instanceof DeclaredType)) {
            return null;
        }
        TypeElement element = (TypeElement) processingEnvironment.getTypeUtils().asElement(type);
        List<? extends TypeParameterElement> genericParameters = element.getTypeParameters();
        List<? extends TypeMirror> actualTypes = ((DeclaredType) type).getTypeArguments();

        TypeElement earsedType = (TypeElement) processingEnvironment.getTypeUtils().asElement(processingEnvironment.getTypeUtils().erasure(type));
        GenericTypeMetadata metadata = new GenericTypeMetadata(element, genericName);

        for (int i = 0; i < actualTypes.size(); i++) {
            TypeParameterElement typeParameterElement = genericParameters.get(i);
            String childGenericName = typeParameterElement.getSimpleName().toString();
            TypeMirror childActualType = actualTypes.get(i);
            GenericTypeMetadata childGeneric = processType(childActualType, childGenericName);
            metadata.addGeneric(childGeneric);
        }
        return metadata;
    }
}
