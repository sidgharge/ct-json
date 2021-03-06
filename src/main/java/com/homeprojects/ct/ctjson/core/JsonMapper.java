package com.homeprojects.ct.ctjson.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

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
import com.homeprojects.ct.ctjson.core.parser.JsonParser;
import com.homeprojects.ct.ctjson.core.serializer.IntegerSerializer;
import com.homeprojects.ct.ctjson.core.serializer.ListSerializer;
import com.homeprojects.ct.ctjson.core.serializer.ObjectParser;
import com.homeprojects.ct.ctjson.core.serializer.Serializer;
import com.homeprojects.ct.ctjson.core.serializer.StringSerializer;

@SuppressWarnings({"rawtypes", "unchecked"})
public class JsonMapper {
	
	private final Map<String, PrimitiveDeserializer> primitiveDeserializers;
	
	private final Map<Class, Deserializer> deserializers;
	
	private final Map<Class, ArrayDeserializer> arrayDeserializers;
	
	private final Map<Class, Serializer> serializers;

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
		
		loadGeneratedDeserializers();
		
		for (Deserializer deserializer : deserializers.values()) {
			deserializer.setJsonMapper(this);
		}
		
		serializers = new HashMap<>();
		serializers.put(String.class, new StringSerializer());
		serializers.put(Iterable.class, new ListSerializer());
		serializers.put(Integer.class, new IntegerSerializer());
		
		loadGeneratedSerializers();
		
		for (Serializer serializer : serializers.values()) {
			serializer.setJsonMapper(this);
		}
	}

	private void loadGeneratedDeserializers() {
		ServiceLoader<Deserializer> loader = ServiceLoader.load(Deserializer.class);
		for (Deserializer deserializer : loader) {
			deserializers.put(deserializer.getType(), deserializer);
		}
	}
	
	private void loadGeneratedSerializers() {
		ServiceLoader<Serializer> loader = ServiceLoader.load(Serializer.class);
		for (Serializer serializer : loader) {
			serializers.put(serializer.getType(), serializer);
		}
	}
	
	public <T extends JsonElement, R> R deserialize(T element, Class<R> clazz) {
		Deserializer deserializer = deserializers.get(clazz);
		return (R) deserializer.deserialize(element);
	}
	
	public <R> R deserialize(String json, Class<R> clazz) {
		JsonElement element = new JsonParser(json).parse();
		return deserialize(element, clazz);
	}
	
	public <R, S> R deserializeArray(String json, Class<R> containerClazz, Class<S> elementClass) {
		JsonElement element = new JsonParser(json).parse();
		return deserializeArray(element, containerClazz, elementClass);
	}
	
	public <T extends JsonElement, R, S> R deserializeArray(T element, Class<R> containerClazz, Class<S> elementClass) {
		ArrayDeserializer deserializer = arrayDeserializers.get(containerClazz);
		Deserializer elementDeserializer = deserializers.get(elementClass);
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
	
	public void registerDeserializer(Class clazz, Deserializer deserializer) {
		deserializer.setJsonMapper(this);
		deserializers.put(clazz, deserializer);
	}
	
	public JsonElement serialize(Object object) {
		Serializer serializer = getSerializer(object);
		return serializer.serialize(object);
	}
	
	public String toJson(Object object) {
		Serializer serializer = getSerializer(object);
		
		JsonElement element = serializer.serialize(object);
		return new ObjectParser().parse(element);
	}

	private Serializer getSerializer(Object object) {
		Serializer serializer = serializers.get(object.getClass());
		if(serializer != null) {
			return serializer;
		}
		if(Iterable.class.isAssignableFrom(object.getClass())) {
			return serializers.get(Iterable.class);
		}
		return serializer;
	}
	
	public JsonValue serializeByte(byte value) {
		return new JsonValue(Byte.toString(value), JsonValueType.NUMBER);
	}
	
	public JsonValue serializeInt(int value) {
		return new JsonValue(Integer.toString(value), JsonValueType.NUMBER);
	}

	public JsonValue serializeBoolean(boolean value) {
		return new JsonValue(Boolean.toString(value), JsonValueType.BOOLEAN);
	}
	
	public void registerSerializer(Serializer serializer) {
		serializer.setJsonMapper(this);
		serializers.put(serializer.getType(), serializer);
	}
}
