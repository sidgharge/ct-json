package com.homeprojects.ct.ctjson.processors;

import com.homeprojects.ct.ctjson.GenericTypeMetadata;
import com.homeprojects.ct.ctjson.core.RuntimeGenericTypeMetadata;
import com.homeprojects.ct.ctjson.core.deserializer.GenericTypesManager;
import com.squareup.javapoet.*;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GenericTypesManagerGenerator {

    private final Map<String, GenericTypeMetadata> map;

    private final ProcessingEnvironment env;

    public GenericTypesManagerGenerator(Map<String, GenericTypeMetadata> map, ProcessingEnvironment env) {
        this.map = map;
        this.env = env;
    }

    public void generate() {
        TypeSpec typeSpec = TypeSpec.classBuilder("SimpleGenericTypesManager")
//                .addAnnotation(GenericType.class) // For Regsistering as Implementation Service
                .addModifiers(Modifier.PUBLIC)
                .superclass(GenericTypesManager.class)
                .addMethod(getConstructor())
                .build();

        JavaFile file = JavaFile.builder("com.homeprojects.ct.ctjson.generated", typeSpec).build();
        try {
            file.writeTo(env.getFiler());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private MethodSpec getConstructor() {
        return MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addCode(CodeBlock.builder().addStatement("super()").build())
                .addCode(getConstructorBodyWithMapPopulated())
                .build();
    }

    private CodeBlock getConstructorBodyWithMapPopulated() {
        CodeBlock.Builder builder = CodeBlock.builder();
        map.forEach((key, metadata) -> toCode(key, metadata, builder));
        return builder.build();
    }

    private void toCode(String key, GenericTypeMetadata metadata, CodeBlock.Builder builder) {
        builder.addStatement("add($S, $L)", key, toCode(metadata));
    }

    private CodeBlock toCode(GenericTypeMetadata metadata) {
        CodeBlock.Builder builder = CodeBlock.builder();
        builder.add("new $T($T.class, $S" + (metadata.getGenerics().isEmpty() ? "" : ", \n"),
                RuntimeGenericTypeMetadata.class,
                env.getTypeUtils().erasure(metadata.getClazz().asType()),
                metadata.getGenericName());

        for (int i = 0; i < metadata.getGenerics().size(); i++) {
            GenericTypeMetadata generic = metadata.getGenerics().get(i);
            builder.add(toCode(generic));
            if (i < metadata.getGenerics().size() - 1) {
                builder.add(", \n");
            }
        }
        builder.add(")");
        return builder.build();
    }
}
