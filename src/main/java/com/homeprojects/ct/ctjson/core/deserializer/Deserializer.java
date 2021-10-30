package com.homeprojects.ct.ctjson.core.deserializer;

import com.homeprojects.ct.ctjson.core.JsonMapper;

public interface Deserializer<T> {

	T deserialize(String json);
	
	T initialize();
	
	void setValue(String fieldName, Object value);
	
	Class<T> getType();
	
	void setJsonMapper(JsonMapper mapper);
}
