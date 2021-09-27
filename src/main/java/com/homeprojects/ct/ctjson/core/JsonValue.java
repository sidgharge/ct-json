package com.homeprojects.ct.ctjson.core;

public class JsonValue implements JsonElement {

	private final String value;

	private final JsonValueType type;

	public JsonValue(String value, JsonValueType type) {
		this.value = value;
		this.type = type;
	}
	
	public String getValue() {
		return value;
	}
	
	public JsonValueType getType() {
		return type;
	}

	@Override
	public String toString() {
		return type == JsonValueType.STRING ? "\"" + value + "\"" : value;
	}

}
