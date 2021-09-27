package com.homeprojects.ct.ctjson.processors;

import javax.annotation.processing.Processor;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;

import com.google.auto.service.AutoService;
import com.homeprojects.ct.ctjson.annotations.GeneratedDeserialzer;
import com.homeprojects.ct.ctjson.core.deserializer.Deserializer;
import com.homeprojects.di.processors.ImplementationFileProccessor;

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class GeneratedDeserializerProcessor extends ImplementationFileProccessor {

	public GeneratedDeserializerProcessor() {
		intefaceClazz = Deserializer.class;
		annotation = GeneratedDeserialzer.class;
	}
}