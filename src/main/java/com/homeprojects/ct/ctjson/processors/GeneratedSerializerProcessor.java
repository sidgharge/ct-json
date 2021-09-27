package com.homeprojects.ct.ctjson.processors;

import javax.annotation.processing.Processor;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;

import com.google.auto.service.AutoService;
import com.homeprojects.ct.ctjson.annotations.GeneratedSerialzer;
import com.homeprojects.ct.ctjson.core.serializer.Serializer;
import com.homeprojects.di.processors.ImplementationFileProccessor;

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class GeneratedSerializerProcessor extends ImplementationFileProccessor {

	public GeneratedSerializerProcessor() {
		intefaceClazz = Serializer.class;
		annotation = GeneratedSerialzer.class;
	}
}