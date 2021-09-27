package com.homeprojects.ct.ctjson.core.serializer;

import com.homeprojects.ct.ctjson.core.JsonElement;
import com.homeprojects.ct.ctjson.core.JsonValue;
import com.homeprojects.ct.ctjson.core.JsonValueType;

public class StringSerializer extends AbstractSerializer<String> {

	@Override
	public JsonElement serialize(String object) {
		return new JsonValue(object, JsonValueType.STRING);
	}

	@Override
	public Class getType() {
		return String.class;
	}
}
