package com.homeprojects.ct.ctjson.core.deserializer;

import com.homeprojects.ct.ctjson.core.JsonMapper;
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
		return parser.parse(this);
	}
	
	@Override
	public T deserialize(JsonParser parser) {
		return parser.parse(this);
	}
	
	@Override
	public void setValue(T object, String key, JsonParser parser) {}
	
	@Override
	public T initialize() {
		return null;
	}
}
