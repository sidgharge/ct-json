package com.homeprojects.ct.ctjson.processors;

import java.io.IOException;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.type.TypeKind;

import com.homeprojects.ct.ctjson.PojoMetadata;
import com.homeprojects.ct.ctjson.Property;
import com.homeprojects.ct.ctjson.core.JsonElement;
import com.homeprojects.ct.ctjson.core.JsonMapper;
import com.homeprojects.ct.ctjson.core.JsonNode;
import com.homeprojects.ct.ctjson.core.deserializer.Deserializer;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
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
				.addModifiers(Modifier.PUBLIC)
				.addSuperinterface(getSuperInterface())
				.addField(getMapperField())
				.addMethod(getConstructor())
				.addMethod(getDeserializeMethod());
		
		JavaFile file = JavaFile.builder(getPackage(), builder.build()).build();

		try {
			file.writeTo(env.getFiler());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String getPackage() {
		return ((PackageElement)metadata.getElement().getEnclosingElement()).getQualifiedName().toString();
	}

	private FieldSpec getMapperField() {
		return FieldSpec.builder(JsonMapper.class, "mapper", Modifier.PRIVATE, Modifier.FINAL).build();
	}

	private MethodSpec getConstructor() {
		return MethodSpec.constructorBuilder()
				.addModifiers(Modifier.PUBLIC)
				.addParameter(ParameterSpec.builder(JsonMapper.class, "mapper").build())
				.addCode(getConstructorBody())
				.build();
	}

	private CodeBlock getConstructorBody() {
		return CodeBlock.builder()
			.addStatement("this.mapper = mapper")
			.addStatement("mapper.registerDeserializer($T.class, this)", metadata.getElement())
			.build();
	}

	private MethodSpec getDeserializeMethod() {
		return MethodSpec.methodBuilder("deserialize")
			.addAnnotation(Override.class)
			.addModifiers(Modifier.PUBLIC)
			.returns(TypeName.get(metadata.getElement().asType()))
			.addParameter(ParameterSpec.builder(JsonElement.class, "element").build())
			.addCode(getDeserializeMethodBody())
			.build();
	}

	private CodeBlock getDeserializeMethodBody() {
		CodeBlock.Builder builder = CodeBlock.builder();
		if(metadata.isIterable()) {
			return builder.build();
		}
		builder.addStatement("$T node = (JsonNode) element", JsonNode.class);
		builder.addStatement("$T object = new $T()", metadata.getElement(), metadata.getElement());
		
		for (Property property: metadata.getProperties()) {
			TypeKind kind = property.getField().asType().getKind();
			if(kind.isPrimitive()) {
				builder.addStatement(getPrimitiveDeserializeStatement(property, kind));
			} else {
				String propertyName = property.getField().getSimpleName().toString();
				builder.addStatement("object.$L(mapper.deserialize(node.getValue($S), $T.class))", property.getSetterMethodName(), propertyName, property.getField());
			}
		}
		builder.addStatement("return object");
		return builder.build();
	}

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

	private TypeName getSuperInterface() {
		TypeName type = TypeName.get(metadata.getElement().asType());
		return ParameterizedTypeName.get(
				ClassName.get(Deserializer.class),
				ClassName.get(JsonElement.class),
				type
		);
	}

	private String getClassName() {
		return metadata.getElement().getSimpleName().toString() + "GeneratedDeserializer";
	}
}
