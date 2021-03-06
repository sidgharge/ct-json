package com.homeprojects.ct.ctjson;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.TypeElement;

public class PojoMetadata {

	private boolean isIterable = true;
	
	private TypeElement element;

	private final List<Property> properties = new ArrayList<>();

	public boolean isIterable() {
		return isIterable;
	}

	public void setIterable(boolean isIterable) {
		this.isIterable = isIterable;
	}

	public List<Property> getProperties() {
		return properties;
	}

	public void addProperty(Property property) {
		this.properties.add(property);
	}

	public void setElement(TypeElement element) {
		this.element = element;
	}
	
	public TypeElement getElement() {
		return element;
	}
}
