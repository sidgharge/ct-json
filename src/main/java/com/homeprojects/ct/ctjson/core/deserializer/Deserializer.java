package com.homeprojects.ct.ctjson.core.deserializer;

import com.homeprojects.ct.ctjson.core.JsonElement;
import com.homeprojects.ct.ctjson.core.JsonMapper;

public interface Deserializer<T extends JsonElement, R> {

	R deserialize(T element);
	
	Class<R> getType();
	
	void setJsonMapper(JsonMapper mapper);
}
