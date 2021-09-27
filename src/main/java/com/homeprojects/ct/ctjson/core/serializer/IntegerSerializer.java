package com.homeprojects.ct.ctjson.core.serializer;

import com.homeprojects.ct.ctjson.core.JsonElement;
import com.homeprojects.ct.ctjson.core.JsonValue;
import com.homeprojects.ct.ctjson.core.JsonValueType;

@SuppressWarnings("rawtypes")
public class IntegerSerializer extends AbstractSerializer<Integer> {

	@Override
	public JsonElement serialize(Integer object) {
		return new JsonValue(object.toString(), JsonValueType.NUMBER);
	}

	@Override
	public Class getType() {
		return Integer.class;
	}

}
