package com.homeprojects.ct.ctjson.core;

public class JsonKeyValue {

	private String key;

	private JsonElement value;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public JsonElement getValue() {
		return value;
	}

	public void setValue(JsonElement value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "\"" + key + "\": " + value;
	}

}
