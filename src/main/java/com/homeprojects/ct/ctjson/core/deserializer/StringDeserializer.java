package com.homeprojects.ct.ctjson.core.deserializer;

import com.homeprojects.ct.ctjson.core.JsonValue;

public class StringDeserializer implements Deserializer<JsonValue, String> {

	@Override
	public String deserialize(JsonValue value) {
		return value.getValue();
	}

}
