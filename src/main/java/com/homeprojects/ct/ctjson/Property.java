package com.homeprojects.ct.ctjson;

import javax.lang.model.element.VariableElement;

public class Property {

	private VariableElement field;
	
	private boolean isIterable;

	private String setterMethodName;

	public Property(VariableElement field, boolean isIterable, String setterMethodName) {
		this.field = field;
		this.isIterable = isIterable;
		this.setterMethodName = setterMethodName;
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
}
