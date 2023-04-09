package com.homeprojects.ct.ctjson.core;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class JsonArray implements JsonElement {

	private final List<JsonElement> elements = new ArrayList<>();

	public void addElement(JsonElement element) {
		elements.add(element);
	}
	
	public List<JsonElement> getElements() {
		return elements;
	}

	@Override
	public String toString() {
		String string = elements.stream().map(Object::toString).collect(Collectors.joining(", "));
		return "[" + string + "]";
	}
	
}
