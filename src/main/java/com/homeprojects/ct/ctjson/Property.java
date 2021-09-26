package com.homeprojects.ct.ctjson;

import javax.lang.model.element.VariableElement;

public class Property {

	private VariableElement field;

	private String setterMethodName;

	public Property(VariableElement field, String setterMethodName) {
		this.field = field;
		this.setterMethodName = setterMethodName;
	}

	public VariableElement getField() {
		return field;
	}

	public String getSetterMethodName() {
		return setterMethodName;
	}
}
