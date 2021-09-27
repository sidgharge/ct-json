package com.homeprojects.ct.ctjson.core.deserializer;

import com.homeprojects.ct.ctjson.core.JsonValue;

public class StringDeserializer extends AbstractDeserializer<JsonValue, String> {

	@Override
	public String deserialize(JsonValue value) {
		return value.getValue();
	}

	@Override
	public Class<String> getType() {
		return String.class;
	}
}
