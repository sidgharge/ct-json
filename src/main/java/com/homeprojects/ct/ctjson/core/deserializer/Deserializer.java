package com.homeprojects.ct.ctjson.core.deserializer;

import com.homeprojects.ct.ctjson.core.JsonElement;

public interface Deserializer<T extends JsonElement, R> {

	R deserialize(T element);
}
