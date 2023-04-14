package com.homeprojects.ct.ctjson.core.deserializer;

import com.homeprojects.ct.ctjson.core.RuntimeGenericTypeMetadata;
import com.homeprojects.ct.ctjson.core.parser.JsonParser;

public class StringDeserializer extends AbstractDeserializer<String> {

	@Override
	public String deserialize(String value) {
		return value;
	}
	
	@Override
	public String deserialize(JsonParser parser) {
		return parser.getNextString();
	}

	@Override
	public String deserialize(String json, RuntimeGenericTypeMetadata metadata) {
		return deserialize(json);
	}

	@Override
	public String deserialize(JsonParser parser, RuntimeGenericTypeMetadata metadata) {
		return deserialize(parser);
	}

	@Override
	public Class<String> getType() {
		return String.class;
	}
}
