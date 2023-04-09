package com.homeprojects.ct.ctjson.processors;

import com.google.auto.service.AutoService;
import com.homeprojects.ct.ctjson.GenericTypeFinder;
import com.homeprojects.ct.ctjson.GenericTypeMetadata;
import com.homeprojects.ct.ctjson.annotations.GenericType;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class GenericTypeProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Map<String, GenericTypeMetadata> metadataMap = new GenericTypeFinder(processingEnv, roundEnv).find();
        new GenericTypesManagerGenerator(metadataMap, processingEnv).generate();
        return false;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> set = new HashSet<>();
        set.add(GenericType.class.getName());
        return set;
    }
}
