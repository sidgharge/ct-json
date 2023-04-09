package com.homeprojects.ct.ctjson.core.deserializer;

import com.homeprojects.ct.ctjson.core.JsonMapper;
import com.homeprojects.ct.ctjson.core.RuntimeGenericTypeMetadata;
import com.homeprojects.ct.ctjson.core.parser.JsonParser;

public interface Deserializer<T> {

	T deserialize(String json);
	
	T deserialize(JsonParser parser);

	T deserialize(String json, RuntimeGenericTypeMetadata metadata);

	T initialize();
	
	void setValue(T object, String key, JsonParser parser);
	
	Class<T> getType();
	
	void setJsonMapper(JsonMapper mapper);

}
