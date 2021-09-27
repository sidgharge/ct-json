package com.homeprojects.ct.ctjson.core.serializer;

import com.homeprojects.ct.ctjson.core.JsonMapper;

public abstract class AbstractSerializer<T> implements Serializer<T> {

	protected JsonMapper mapper;

	@Override
	public void setJsonMapper(JsonMapper mapper) {
		this.mapper = mapper;
	}
}


