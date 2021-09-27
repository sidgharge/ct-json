package com.homeprojects.ct.ctjson.processors;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;

import com.google.auto.service.AutoService;
import com.homeprojects.ct.ctjson.PojoMetadata;
import com.homeprojects.ct.ctjson.PojoMetadataFinder;
import com.homeprojects.ct.ctjson.annotations.JsonDeserialize;
import com.homeprojects.ct.ctjson.annotations.JsonSerialize;

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class JsonDeserializeProcessor extends AbstractProcessor {

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		List<PojoMetadata> list = new PojoMetadataFinder(processingEnv, roundEnv).find();
		for (PojoMetadata metadata : list) {
			new PojoDeserializerGenerator(metadata, processingEnv).generate();
			new PojoSerializerGenerator(metadata, processingEnv).generate();
		}
		return false;
	}

	@Override
	public Set<String> getSupportedAnnotationTypes() {
		Set<String> set = new HashSet<>();
		set.add(JsonDeserialize.class.getName());
		set.add(JsonSerialize.class.getName());
		return set;
	}
}
