package com.homeprojects.ct.ctjson;

import javax.lang.model.element.VariableElement;

public class Property {

	private final VariableElement field;
	
	private final boolean isIterable;

	private String setterMethodName;
	
	private String getterMethodName;

	public Property(VariableElement field, boolean isIterable) {
		this.field = field;
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
	
	
}
