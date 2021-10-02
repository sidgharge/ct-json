package com.homeprojects.ct.ctjson2.processors;

import java.io.IOException;
import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import com.homeprojects.ct.ctjson.PojoMetadata;
import com.homeprojects.ct.ctjson.Property;
import com.homeprojects.ct.ctjson.annotations.GeneratedDeserialzer;
import com.homeprojects.ct.ctjson2.core.JsonMapper2;
import com.homeprojects.ct.ctjson2.core.JsonParser2;
import com.homeprojects.ct.ctjson2.core.deserializer.Deserializer;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

public class PojoDeserializerGenerator2 {

	private final PojoMetadata metadata;
	
	private final ProcessingEnvironment env;
	
	private TypeMirror stringType;
	
	public PojoDeserializerGenerator2(PojoMetadata metadata, ProcessingEnvironment env) {
		this.metadata = metadata;
		this.env = env;
		this.stringType = env.getElementUtils().getTypeElement("java.lang.String").asType();
	}
	
	public void generate() {
		String className = getClassName();
		TypeSpec.Builder builder = TypeSpec.
				classBuilder(className)
				.addAnnotation(GeneratedDeserialzer.class)
				.addModifiers(Modifier.PUBLIC)
				.superclass(getSuperClass())
				.addMethod(getInitializeMethod())
				.addMethod(getSetValueMethod())
				.addMethod(getGetTypeMethod())
				.addMethod(getIsIterableMethod());
		
		JavaFile file = JavaFile.builder(getPackage(), builder.build()).build();

		try {
			file.writeTo(env.getFiler());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private MethodSpec getGetTypeMethod() {
		ParameterizedTypeName returnType = ParameterizedTypeName.get(ClassName.get(Class.class), TypeName.get(metadata.getErasedType()));
		return MethodSpec.methodBuilder("getType")
			.addAnnotation(Override.class)
			.addModifiers(Modifier.PUBLIC)
			.returns(returnType)
			.addStatement("return $T.class", metadata.getErasedType())
			.build();
	}
	
	private MethodSpec getIsIterableMethod() {
		return MethodSpec.methodBuilder("isIterable")
			.addAnnotation(Override.class)
			.addModifiers(Modifier.PUBLIC)
			.returns(boolean.class)
			.addStatement("return $L", metadata.isIterable())
			.build();
	}

	private String getPackage() {
		return ((PackageElement)metadata.getElement().getEnclosingElement()).getQualifiedName().toString();
	}

	private MethodSpec getInitializeMethod() {
		TypeName type = TypeName.get(metadata.getErasedType());
		return MethodSpec.methodBuilder("initialize")
				.addModifiers(Modifier.PUBLIC)
				.addAnnotation(Override.class)
				.returns(type)
				.addCode(getInitializeMethodBody())
				.build();
	}

	private CodeBlock getInitializeMethodBody() {
		return CodeBlock.builder()
			.addStatement("return new $T()", metadata.getErasedType())
			.build();
	}

	private MethodSpec getSetValueMethod() {
		TypeName type = TypeName.get(metadata.getErasedType());
		return MethodSpec.methodBuilder("setValue")
			.addAnnotation(Override.class)
			.addModifiers(Modifier.PUBLIC)
			.returns(void.class)
			.addParameter(ParameterSpec.builder(type, "object").build())
			.addParameter(ParameterSpec.builder(String.class, "key").build())
			.addParameter(ParameterSpec.builder(JsonParser2.class, "parser").build())
			.addCode(getSetValueMethodBody())
			.build();
	}

	private CodeBlock getSetValueMethodBody() {
		CodeBlock.Builder builder = CodeBlock.builder();
		builder.beginControlFlow("switch(key)");
		
		for (Property property: metadata.getProperties()) {
			builder.add("case $S:\n", property.getField().getSimpleName().toString());
			CodeBlock block = getSetFieldCode(property);
			builder.addStatement("object.$L($L)", property.getSetterMethodName(), block);
			builder.addStatement("break");
		}
		builder.add("default:\n");
		builder.addStatement("parser.skipValue()");
		builder.addStatement("break");
		builder.endControlFlow();
		
		return builder.build();
	}

	private CodeBlock getSetFieldCode(Property property) {
		CodeBlock.Builder builder = CodeBlock.builder();
		builder.add("parser.");
		TypeMirror propertyType = property.getField().asType();
		TypeKind kind = propertyType.getKind();
		if(kind.isPrimitive()) {
			builder.add(getPrimitiveCode(property, kind));
		} else if(env.getTypeUtils().isSameType(propertyType, stringType)) {
			builder.add("getNextString()");
		} else if(kind.equals(TypeKind.TYPEVAR)) {
//			System.out.println(propertyType);deserializeGenericObject
			builder.add("deserializeGenericObject()");
		} else {
			builder.add("deserialize($T.class)", env.getTypeUtils().erasure(propertyType));
		}
		return builder.build();
	}

	private CodeBlock getPrimitiveCode(Property property, TypeKind kind) {
		if(kind.equals(TypeKind.INT)) {
			return CodeBlock.of("toInt(parser.getNextNumber())");
		}
		if(kind.equals(TypeKind.BOOLEAN)) {
			return CodeBlock.of("getNextBoolean()");
		}
		return CodeBlock.builder().build(); // TODO Handle all cases
	}

	private void handleIterableProperty(Property property, CodeBlock.Builder builder) {
		TypeMirror type = property.getField().asType();
		if(!(type instanceof DeclaredType)) {
			return;
		}

		DeclaredType dt = (DeclaredType) type;
		List<? extends TypeMirror> arguments = dt.getTypeArguments();
		if(arguments.isEmpty()) {
			return;
		}
		
		TypeMirror containerType = env.getTypeUtils().erasure(type);
		TypeMirror elementType = arguments.get(0);
		String propertyName = property.getField().getSimpleName().toString();
		builder.addStatement("object.$L(mapper.deserializeArray(node.getValue($S), $T.class, $T.class))", property.getSetterMethodName(), propertyName, containerType, elementType);
	}

	// TODO include other primitives
	private CodeBlock getPrimitiveDeserializeStatement(Property property, TypeKind kind) {
		String propertyName = property.getField().getSimpleName().toString();
		String methodName = null;
		if(kind.equals(TypeKind.INT)) {
			methodName = "deserializeAsInt";
		} else if(kind.equals(TypeKind.BYTE)) {
			methodName = "deserializeAsByte";
		} else if(kind.equals(TypeKind.BOOLEAN)) {
			methodName = "deserializeAsBoolean";
		}
		return CodeBlock.of("object.$L(mapper.$L(node.getValue($S)))", property.getSetterMethodName(), methodName, propertyName);
	}

	private TypeName getSuperClass() {
		TypeName type = TypeName.get(metadata.getErasedType());
		return ParameterizedTypeName.get(
				ClassName.get(Deserializer.class),
				type
		);
	}

	private String getClassName() {
		return metadata.getElement().getSimpleName().toString() + "GeneratedDeserializer2";
	}
}
