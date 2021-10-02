package com.homeprojects.ct.ctjson;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

public class Property {

	private final VariableElement field;
	
	private final TypeMirror erasedType;
	
	private final boolean isIterable;

	private String setterMethodName;
	
	private String getterMethodName;

	public Property(VariableElement field, TypeMirror erasedType, boolean isIterable) {
		this.field = field;
		this.erasedType = erasedType;
		this.isIterable = isIterable;
	}

	public VariableElement getField() {
		return field;
	}

	public String getSetterMethodName() {
		return setterMethodName;
	}
	
	public boolean isIterable() {
		return isIterable;
	}

	public String getGetterMethodName() {
		return getterMethodName;
	}

	public void setGetterMethodName(String getterMethodName) {
		this.getterMethodName = getterMethodName;
	}

	public void setSetterMethodName(String setterMethodName) {
		this.setterMethodName = setterMethodName;
	}
	
	public TypeMirror getErasedType() {
		return erasedType;
	}
}
