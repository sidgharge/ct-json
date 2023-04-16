package com.homeprojects.ct.ctjson.processors;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import com.homeprojects.ct.ctjson.PojoMetadata;
import com.homeprojects.ct.ctjson.Property;
import com.homeprojects.ct.ctjson.annotations.GeneratedDeserialzer;
import com.homeprojects.ct.ctjson.core.RuntimeGenericTypeMetadata;
import com.homeprojects.ct.ctjson.core.deserializer.AbstractDeserializer;
import com.homeprojects.ct.ctjson.core.parser.JsonParser;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

public class PojoDeserializerGenerator {

	private final PojoMetadata metadata;
	
	private final ProcessingEnvironment env;
	
	public PojoDeserializerGenerator(PojoMetadata metadata, ProcessingEnvironment env) {
		this.metadata = metadata;
		this.env = env;
	}
	
	public void generate() {
		String className = getClassName();
		TypeSpec.Builder builder = TypeSpec.
				classBuilder(className)
				.addAnnotation(GeneratedDeserialzer.class)
				.addModifiers(Modifier.PUBLIC)
				.superclass(getSuperClass())
				.addMethod(getGetTypeMethod())
				.addMethod(getInitializeMethod())
				.addMethod(getSetValueMethod());
		
		JavaFile file = JavaFile.builder(getPackage(), builder.build()).build();

		try {
			file.writeTo(env.getFiler());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private MethodSpec getInitializeMethod() {
		return MethodSpec.methodBuilder("initialize")
				.addAnnotation(Override.class)
				.addModifiers(Modifier.PUBLIC)
				.returns(TypeName.get(metadata.getErasedType()))
				.addCode(getInitializeMethodBody())
				.build();
	}
	
	private CodeBlock getInitializeMethodBody() {
		return CodeBlock.of("return new $T();", metadata.getErasedType());
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
	
	private MethodSpec getSetValueMethod() {
		TypeName type = TypeName.get(metadata.getErasedType());
		return MethodSpec.methodBuilder("setValue")
			.addAnnotation(Override.class)
			.addModifiers(Modifier.PUBLIC)
			.returns(void.class)
			.addParameter(ParameterSpec.builder(type, "object").build())
			.addParameter(ParameterSpec.builder(String.class, "key").build())
			.addParameter(ParameterSpec.builder(JsonParser.class, "parser").build())
			.addParameter(ParameterSpec.builder(RuntimeGenericTypeMetadata.class, "metadata").build())
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
		builder.addStatement("break");
		builder.endControlFlow();
		
		return builder.build();
	}
	
	private CodeBlock getSetFieldCode(Property property) {
		CodeBlock.Builder builder = CodeBlock.builder();
		TypeMirror propertyType = property.getField().asType();
		System.out.println("FieldType: " + propertyType + ":::" + propertyType.getKind());
		TypeKind kind = propertyType.getKind();
		if(kind.isPrimitive()) {
			builder.add(getPrimitiveCode(kind));
		} else if(kind.equals(TypeKind.TYPEVAR)) {
			List<Integer> indices = getGenericIndices(Arrays.asList(propertyType));
			builder.add("mapper.deserialize(parser, metadata.getGeneric($L))", indices.get(0));
		} else {
			setFieldNonPrimitive(property, builder, ((DeclaredType) propertyType).getTypeArguments());
		}
		return builder.build();
	}

	private void setFieldNonPrimitive(Property property, CodeBlock.Builder builder, List<? extends TypeMirror> typeArguments) {
		if (typeArguments.size() == 0) {
			builder.add("mapper.deserialize(parser, $T.class)", property.getErasedType());
			return;
		}
		List<Integer> indices = getGenericIndices(typeArguments);
		CodeBlock.Builder metadataBuilder = CodeBlock.builder()
				.add("new $T($T.class, $S, ", RuntimeGenericTypeMetadata.class, property.getErasedType(), property.getErasedType());
		for (int i = 0; i < indices.size(); i++) {
			Integer index = indices.get(i);
			if (i > 0) {
				metadataBuilder.add(", ");
			}
			if (index > -1) {
				metadataBuilder.add("metadata.getGeneric($L)", index);
			} else {
				metadataBuilder.add("new $T($T.class, $S, metadata)", RuntimeGenericTypeMetadata.class, typeArguments.get(i), typeArguments.get(i));
			}
		}
		metadataBuilder.add(")");
		builder.add("mapper.deserialize(parser, $T.class, $L)", property.getErasedType(), metadataBuilder.build());
	}

	private List<Integer> getGenericIndices(List<? extends TypeMirror> typeArguments) {
		List<Integer> indices = new ArrayList<>();

		for (TypeMirror typeArgument : typeArguments) {
			boolean added = false;
			for (int i = 0; i < metadata.getElement().getTypeParameters().size(); i++) {
				TypeParameterElement tp = metadata.getElement().getTypeParameters().get(i);
				String typeName = tp.getSimpleName().toString();
				if(!typeName.equals(typeArgument.toString())){
					continue;
				}
				indices.add(i);
				added = true;
				break;
			}
			if (!added) {
				indices.add(-1);
			}
		}
		return indices;
	}

	private CodeBlock getPrimitiveCode(TypeKind kind) {
		switch (kind) {
		case INT:
			return CodeBlock.of("parser.toInt(parser.getNextNumber())");
		case SHORT:
			return CodeBlock.of("parser.toShort(parser.getNextNumber())");
		case BYTE:
			return CodeBlock.of("parser.toByte(parser.getNextNumber())");
		case DOUBLE:
			return CodeBlock.of("parser.toDouble(parser.getNextNumber())");
		case FLOAT:
			return CodeBlock.of("parser.toFloat(parser.getNextNumber())");
		case BOOLEAN:
			return CodeBlock.of("parser.getNextBoolean()");
		default:
			return CodeBlock.builder().build();
		}
	}

	private String getPackage() {
		return ((PackageElement)metadata.getElement().getEnclosingElement()).getQualifiedName().toString();
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
				ClassName.get(AbstractDeserializer.class),
				type
		);
	}

	private String getClassName() {
		return metadata.getElement().getSimpleName().toString() + "GeneratedDeserializer";
	}
}
