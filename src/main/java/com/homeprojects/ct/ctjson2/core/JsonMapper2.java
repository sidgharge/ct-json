package com.homeprojects.ct.ctjson2.core;

import java.util.HashMap;
import java.util.Map;

import com.homeprojects.ct.ctjson2.core.deserializer.Deserializer2;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class JsonMapper2 {
	
	private final Map<Type, Deserializer2> deserializers;
	
	public JsonMapper2() {
		deserializers = new HashMap<>();
		
//		Deserializer2 deserializer2 = new MapDeserializer();
//		deserializers.put(deserializer2.getType(), deserializer2);
//		
//		deserializer2 = new ListDeserializer();
//		deserializers.put(deserializer2.getType(), deserializer2);
	}

	public <T> T deserialize(String json, Class<T> clazz) {
		return deserialize(json, new Type(clazz));
	}
	
	public <T> T deserialize(String json, Type type) {
		return (T) deserializers.get(type).deserialize(json, type);
	}
	
	public void registerDeserializer(Deserializer2 deserializer2) {
		deserializer2.setMapper(this);
		deserializers.put(deserializer2.getType(), deserializer2);
	}

	public Deserializer2 getDeserializer(Type type) {
		return deserializers.get(type);
	}

	
}
