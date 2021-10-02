package com.homeprojects.ct.ctjson2.core;

import java.util.HashMap;
import java.util.Map;

import com.homeprojects.ct.ctjson2.core.deserializer.Deserializer;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class JsonMapper2 {
	
	private Map<Class, Deserializer> deserializers = new HashMap<>();

	public <T> T deserialize(String json, Class<T> clazz, Class... genericTypes) {
		return (T) deserializers.get(clazz).deserialize(json, genericTypes);
	}
	
	public void registerDeserializer(Deserializer deserializer) {
		deserializer.setMapper(this);
		deserializers.put(deserializer.getType(), deserializer);
	}

	public Deserializer getDeserializer(Class clazz) {
		return deserializers.get(clazz);
	}

	
}
