package com.homeprojects.ct.ctjson.core.deserializer;

import com.homeprojects.ct.ctjson.core.JsonValue;

public class IntegerDeserializer extends AbstractDeserializer<JsonValue, Integer> {

	@Override
	public Integer deserialize(JsonValue element) {
		return Integer.valueOf(element.getValue());
	}

	@Override
	public Class<Integer> getType() {
		return Integer.class;
	}
}
