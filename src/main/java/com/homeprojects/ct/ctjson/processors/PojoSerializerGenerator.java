package com.homeprojects.ct.ctjson.processors;

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
import com.homeprojects.ct.ctjson.annotations.GeneratedSerialzer;
import com.homeprojects.ct.ctjson.core.JsonElement;
import com.homeprojects.ct.ctjson.core.JsonKeyValue;
import com.homeprojects.ct.ctjson.core.JsonMapper;
import com.homeprojects.ct.ctjson.core.JsonNode;
import com.homeprojects.ct.ctjson.core.serializer.AbstractSerializer;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

public class PojoSerializerGenerator {

	private final PojoMetadata metadata;
	
	private final ProcessingEnvironment env;
	
	public PojoSerializerGenerator(PojoMetadata metadata, ProcessingEnvironment env) {
		this.metadata = metadata;
		this.env = env;
	}
	
	public void generate() {
		String className = getClassName();
		TypeSpec.Builder builder = TypeSpec.
				classBuilder(className)
				.addAnnotation(GeneratedSerialzer.class)
				.addModifiers(Modifier.PUBLIC)
				.superclass(getSuperClass())
				.addMethod(getSerializeMethod())
				.addMethod(getGetTypeMethod());
		
		for (Property property : metadata.getProperties()) {
			builder.addMethod(getMethodForSettingPair(property));
		}
		
		JavaFile file = JavaFile.builder(getPackage(), builder.build()).build();

		try {
			file.writeTo(env.getFiler());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private MethodSpec getMethodForSettingPair(Property property) {
		TypeName type = TypeName.get(metadata.getElement().asType());
		return MethodSpec.methodBuilder(property.getField().getSimpleName().toString())
			.addModifiers(Modifier.PRIVATE)
			.returns(void.class)
			.addParameter(ParameterSpec.builder(type, "object").build())
			.addParameter(ParameterSpec.builder(JsonNode.class, "node").build())
			.addCode(getSettingPairBody(property))
			.build();
	}

	private CodeBlock getSettingPairBody(Property property) {
		CodeBlock.Builder builder = CodeBlock.builder();
		builder.addStatement("$T pair = new $T()", JsonKeyValue.class, JsonKeyValue.class);
		builder.addStatement("pair.setKey($S)", property.getField().getSimpleName().toString());
		
		TypeKind kind = property.getField().asType().getKind();
		if(kind.isPrimitive()) {
			builder.addStatement(getPrimitiveSerializeStatement(property, kind));
		} else {
			builder.addStatement("pair.setValue(mapper.serialize(object.$L()))", property.getGetterMethodName());
		}
		
		builder.addStatement("node.addPair(pair)");
		return builder.build();
	}

	private MethodSpec getGetTypeMethod() {
		return MethodSpec.methodBuilder("getType")
			.addAnnotation(Override.class)
			.addModifiers(Modifier.PUBLIC)
			.returns(Class.class)
			.addStatement("return $T.class", metadata.getElement())
			.build();
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

	private MethodSpec getSerializeMethod() {
		return MethodSpec.methodBuilder("serialize")
			.addAnnotation(Override.class)
			.addModifiers(Modifier.PUBLIC)
			.returns(JsonElement.class)
			.addParameter(ParameterSpec.builder(TypeName.get(metadata.getElement().asType()), "object").build())
			.addCode(getSerializeMethodBody())
			.build();
	}

	private CodeBlock getSerializeMethodBody() {
		CodeBlock.Builder builder = CodeBlock.builder();
		builder.addStatement("$T node = new JsonNode()", JsonNode.class);
		
		for (Property property: metadata.getProperties()) {
			String propertyName = property.getField().getSimpleName().toString();
			builder.addStatement("$L(object, node)", propertyName);
		}
		builder.addStatement("return node");
		return builder.build();
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
	private CodeBlock getPrimitiveSerializeStatement(Property property, TypeKind kind) {
		String methodName = null;
		if(kind.equals(TypeKind.INT)) {
			methodName = "serializeInt";
		} else if(kind.equals(TypeKind.BYTE)) {
			methodName = "serializeByte";
		} else if(kind.equals(TypeKind.BOOLEAN)) {
			methodName = "serializeBoolean";
		}
		return CodeBlock.of("pair.setValue(mapper.$L(object.$L()))", methodName, property.getGetterMethodName());
	}

	private TypeName getSuperClass() {
		TypeName type = TypeName.get(metadata.getElement().asType());
		return ParameterizedTypeName.get(
				ClassName.get(AbstractSerializer.class),
				type
		);
	}

	private String getClassName() {
		return metadata.getElement().getSimpleName().toString() + "GeneratedSerializer";
	}
}
