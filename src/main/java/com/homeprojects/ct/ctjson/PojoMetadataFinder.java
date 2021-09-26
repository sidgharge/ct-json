package com.homeprojects.ct.ctjson;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import com.homeprojects.ct.ctjson.annotations.JsonDeserialize;

public class PojoMetadataFinder {

	private final ProcessingEnvironment processingEnv;
	
	private final RoundEnvironment roundEnv;

	public PojoMetadataFinder(ProcessingEnvironment processingEnv, RoundEnvironment roundEnv) {
		this.processingEnv = processingEnv;
		this.roundEnv = roundEnv;
	}
	
	public List<PojoMetadata> find() {
		Set<? extends Element> set = roundEnv.getElementsAnnotatedWith(JsonDeserialize.class);
		List<PojoMetadata> list = new ArrayList<>();
		for (Element element : set) {
			PojoMetadata metadata = process((TypeElement) element);
			list.add(metadata);
		}
		return list;
	}

	private PojoMetadata process(TypeElement element) {
		PojoMetadata metadata = new PojoMetadata();
		metadata.setIterable(isIterable(element));
		metadata.setElement(element);
		
		List<? extends Element> enclosedElements = element.getEnclosedElements();
		for (Element enclosedField : enclosedElements) {
			processField(element, enclosedField, metadata);
		}
		return metadata;
	}

	private boolean isIterable(TypeElement element) {
		TypeMirror t1 = element.asType();
		TypeMirror t2 = processingEnv.getElementUtils().getTypeElement(Iterable.class.getName()).asType();
		return processingEnv.getTypeUtils().isAssignable(t1, t2);
	}
	
	private void processField(TypeElement element, Element enclosedField, PojoMetadata metadata) {
		if(!enclosedField.getKind().equals(ElementKind.FIELD)) {
			return;
		}
		VariableElement property = (VariableElement) enclosedField;
		String fieldName = property.getSimpleName().toString();
		
		List<String> possibleSetterNames = new ArrayList<>();
		possibleSetterNames.add(getAccessorName("set", fieldName));
		
		if(isBooleanType(property)) {
			// Getter logic
//			if(fieldName.startsWith("is")) {
//				possibleSetterNames.add(fieldName);
//			} else {
//				possibleSetterNames.add(getGetterName("is", fieldName));
//			}
			if(fieldName.startsWith("is") && fieldName.length() > 2) {
				String str = "set" + fieldName.substring(2);
				possibleSetterNames.add(str);
			}
		}
		
		for (Element enclosedMethodElement : element.getEnclosedElements()) {
			if(isMethodSetter(enclosedMethodElement, enclosedField, possibleSetterNames)) {
				String methodName = enclosedMethodElement.getSimpleName().toString();
				metadata.addProperty(new Property(property, methodName));
			}
		}
	}
	
	private boolean isMethodSetter(Element enclosedMethodElement, Element enclosedField, List<String> possibleSetterNames) {
		if(!enclosedMethodElement.getKind().equals(ElementKind.METHOD)) {
			return false;
		}
		ExecutableElement enclosedMethod = (ExecutableElement) enclosedMethodElement;
		List<? extends VariableElement> parameters = enclosedMethod.getParameters();
		if(parameters.size() != 1) {
			return false;
		}
		if(!parameters.get(0).asType().equals(enclosedField.asType())) {
			return false;
		}
		
		return possibleSetterNames.stream()
			.anyMatch(gn -> gn.equals(enclosedMethod.getSimpleName().toString()));
	}
	
	private boolean isBooleanType(VariableElement property) {
//		TypeMirror type = property.asType();
//		TypeMirror type2 = processingEnv.getElementUtils().getTypeElement(boolean.class.getName()).asType();
		
		return property.asType().getKind().equals(TypeKind.BOOLEAN);
	}

	private String getAccessorName(String prefix, String fieldName) {
		if(fieldName.length() == 1) {
			return prefix + fieldName.toUpperCase();
		}
		return prefix + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
	}
}
