package com.homeprojects.ct.ctjson.core.serializer;

import com.homeprojects.ct.ctjson.core.JsonElement;
import com.homeprojects.ct.ctjson.core.JsonMapper;

@SuppressWarnings("rawtypes")
public interface Serializer<T> {

	JsonElement serialize(T object);
	
	Class getType();
	
	void setJsonMapper(JsonMapper mapper);
}
