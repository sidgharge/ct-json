package com.homeprojects.ct.ctjson.core.deserializer;

import com.homeprojects.ct.ctjson.core.RuntimeGenericTypeMetadata;
import com.homeprojects.ct.ctjson.core.parser.JsonParser;

public class IntegerDeserializer extends AbstractDeserializer<Integer> {

	@Override
	public Integer deserialize(String str) {
		return Integer.valueOf(str);
	}

	@Override
	public Integer deserialize(JsonParser parser, RuntimeGenericTypeMetadata metadata) {
		return deserialize(parser.getNextNumber());
	}

	@Override
	public Integer deserialize(String json, RuntimeGenericTypeMetadata metadata) {
		return deserialize(json);
	}

	@Override
	public Integer deserialize(JsonParser parser) {
		return deserialize(parser.getNextNumber());
	}

	@Override
	public Class<Integer> getType() {
		return Integer.class;
	}
}
