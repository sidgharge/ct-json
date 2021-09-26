package com.homeprojects.ct.ctjson.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.homeprojects.ct.ctjson.core.deserializer.ArrayDeserializer;
import com.homeprojects.ct.ctjson.core.deserializer.Deserializer;
import com.homeprojects.ct.ctjson.core.deserializer.IntegerDeserializer;
import com.homeprojects.ct.ctjson.core.deserializer.ListDeserializer;
import com.homeprojects.ct.ctjson.core.deserializer.PrimitiveDeserializer;
import com.homeprojects.ct.ctjson.core.deserializer.PrimitiveDeserializer.BooleanDeserializer;
import com.homeprojects.ct.ctjson.core.deserializer.PrimitiveDeserializer.ByteDeserializer;
import com.homeprojects.ct.ctjson.core.deserializer.PrimitiveDeserializer.IntDeserializer;
import com.homeprojects.ct.ctjson.core.deserializer.PrimitiveDeserializer.ShortDeserializer;
import com.homeprojects.ct.ctjson.core.deserializer.StringDeserializer;

public class JsonMapper {
	
	private final Map<String, PrimitiveDeserializer> primitiveDeserializers;
	
	private final Map<Class<?>, Deserializer<? extends JsonElement, ?>> deserializers;
	
	@SuppressWarnings("rawtypes")
	private final Map<Class, ArrayDeserializer> arrayDeserializers;

	public JsonMapper() {
		this.primitiveDeserializers = new HashMap<>();
		primitiveDeserializers.put("byte", new ByteDeserializer());
		primitiveDeserializers.put("short", new ShortDeserializer());
		primitiveDeserializers.put("int", new IntDeserializer());
		primitiveDeserializers.put("boolean", new BooleanDeserializer());
		
		deserializers = new HashMap<>();
		deserializers.put(String.class, new StringDeserializer());
		deserializers.put(Integer.class, new IntegerDeserializer());
		
		arrayDeserializers = new HashMap<>();
		arrayDeserializers.put(List.class, new ListDeserializer());
	}
	
	@SuppressWarnings("unchecked")
	public <T extends JsonElement, R> R deserialize(T element, Class<R> clazz) {
		Deserializer<T, R> deserializer = (Deserializer<T, R>) deserializers.get(clazz);
		return deserializer.deserialize(element);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <T extends JsonElement, R, S> R deserializeArray(T element, Class<R> containerClazz, Class<S> elementClass) {
		ArrayDeserializer deserializer = arrayDeserializers.get(containerClazz);
		Deserializer<? extends JsonElement, ?> elementDeserializer = deserializers.get(elementClass);
		return (R) deserializer.deserialize((JsonArray)element, elementDeserializer);
	}
	
	public byte deserializeAsByte(JsonElement element) {
		if(element.getClass() != JsonValue.class) {
			// TODO throw exception
		}
		return ((ByteDeserializer) primitiveDeserializers.get("byte")).deserialize((JsonValue) element);
	}
	
	public int deserializeAsInt(JsonElement element) {
		if(element.getClass() != JsonValue.class) {
			// TODO throw exception
		}
		return ((IntDeserializer) primitiveDeserializers.get("int")).deserialize((JsonValue)element);
	}

	public boolean deserializeAsBoolean(JsonElement element) {
		if(element.getClass() != JsonValue.class) {
			// TODO throw exception
		}
		return ((BooleanDeserializer) primitiveDeserializers.get("boolean")).deserialize((JsonValue) element);
	}
	
	public void registerDeserializer(Class<?> clazz, Deserializer<?, ?> deserializer) {
		deserializers.put(clazz, deserializer);
	}
}
