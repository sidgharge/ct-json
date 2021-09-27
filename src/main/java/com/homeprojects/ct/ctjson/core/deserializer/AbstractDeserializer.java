package com.homeprojects.ct.ctjson.core.deserializer;

import com.homeprojects.ct.ctjson.core.JsonElement;
import com.homeprojects.ct.ctjson.core.JsonMapper;

public abstract class AbstractDeserializer<T extends JsonElement, R> implements Deserializer<T, R> {

	protected JsonMapper mapper;
	
	@Override
	public void setJsonMapper(JsonMapper mapper) {
		this.mapper = mapper;
	}
}
