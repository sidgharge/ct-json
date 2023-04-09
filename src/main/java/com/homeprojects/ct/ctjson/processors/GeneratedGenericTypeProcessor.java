package com.homeprojects.ct.ctjson.processors;

import com.google.auto.service.AutoService;
import com.homeprojects.ct.ctjson.annotations.GeneratedDeserialzer;
import com.homeprojects.ct.ctjson.annotations.GeneratedGenericTye;
import com.homeprojects.ct.ctjson.core.deserializer.Deserializer;
import com.homeprojects.ct.ctjson.core.deserializer.GenericTypesManager;
import com.homeprojects.di.processors.ImplementationFileProccessor;

import javax.annotation.processing.Processor;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class GeneratedGenericTypeProcessor extends ImplementationFileProccessor {

	public GeneratedGenericTypeProcessor() {
		intefaceClazz = GenericTypesManager.class;
		annotation = GeneratedGenericTye.class;
	}
}