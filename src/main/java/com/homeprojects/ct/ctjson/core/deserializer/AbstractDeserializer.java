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
		JsonParser<T> parser = new JsonParser<T>(json, this);
		return parser.parse();
	}
}
