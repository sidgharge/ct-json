package com.homeprojects.ct.ctjson.core.deserializer;

import com.homeprojects.ct.ctjson.core.JsonMapper;
import com.homeprojects.ct.ctjson.core.RuntimeGenericTypeMetadata;
import com.homeprojects.ct.ctjson.core.parser.JsonParser;

public abstract class AbstractDeserializer<T> implements Deserializer<T> {

	protected JsonMapper mapper;
	
	@Override
	public void setJsonMapper(JsonMapper mapper) {
		this.mapper = mapper;
	}
	
	@Override
	public T deserialize(String json) {
		JsonParser parser = new JsonParser(json);
		return parser.parse(this, null);
	}
	
	@Override
	public T deserialize(JsonParser parser) {
		return parser.parse(this, null);
	}

	@Override
	public T deserialize(String json, RuntimeGenericTypeMetadata metadata) {
		JsonParser parser = new JsonParser(json);
		return parser.parse(this, metadata);
	}

	@Override
	public T deserialize(JsonParser parser, RuntimeGenericTypeMetadata metadata) {
		return parser.parse(this, metadata);
	}

	@Override
	public void setValue(T object, String key, JsonParser parser, RuntimeGenericTypeMetadata metadata) {}
	
	@Override
	public T initialize() {
		return null;
	}

	protected void getMetadata(RuntimeGenericTypeMetadata metadata) {

	}
}
