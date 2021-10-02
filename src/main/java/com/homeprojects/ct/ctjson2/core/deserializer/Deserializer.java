package com.homeprojects.ct.ctjson2.core.deserializer;

import java.util.Arrays;
import java.util.List;

import com.homeprojects.ct.ctjson2.core.JsonMapper2;
import com.homeprojects.ct.ctjson2.core.JsonParser2;

public abstract class Deserializer<T> {
	
	protected JsonMapper2 mapper;
	
	public T deserialize(String json, Class... types) {
		JsonParser2 parser = new JsonParser2(json, mapper, types);
		return deserialize(json, parser);
	}
	
	public T deserialize(String json, JsonParser2 parser) {
		parser.skipWhiteSpace();
		char c = parser.getCharacter();
		
		if ((isIterable() && c == '{') || (!isIterable() && c == '[')) {
			// TODO Handle throw exception
		}
		if(isIterable()) {
			return startJsonArray(parser);
		} else {
			return startJsonObject(parser);
		}
	}
	
	private T startJsonArray(JsonParser2 parser) {
		T object = initialize();
		
		char c = parser.getCharacter();
		while(c != ']') {
			Object arrayElement = getNextGenericElement(parser);
			c = parser.getCharacter();
			parser.decrement();
		}
		parser.increment();
		parser.incrementGenericIndex();
		return object;
	}

	private Object getNextGenericElement(JsonParser2 parser) {
		parser.getNextCharacter();
		return parser.getNextGenericElement();
	}

	private T startJsonObject(JsonParser2 parser) {
		T object = initialize();
		
		char c = parser.getCharacter();
		while(c != '}') {
			setJsonKeyValuePair(object, parser);
			c = parser.getCharacter();
		}
		parser.increment();
		return object;
	}
	
	private void setJsonKeyValuePair(T object, JsonParser2 parser) {
		char c = parser.getNextCharacter();
		if(c != '"') {
			// TODO throw exception
		}
		String key = parser.getNextString();
		parser.skipWhiteSpace();
		c = parser.getCharacter();
		if(c != ':') {
			// TODO throw exception
		}
		c = parser.getNextCharacter();
		setValue(object, key, parser);
		parser.skipComma();
	}
	
	protected abstract void setValue(T object, String key, JsonParser2 parser);

	protected abstract T initialize();
	
	public abstract Class<T> getType();
	
	protected abstract boolean isIterable();
	
	public void setMapper(JsonMapper2 mapper) {
		this.mapper = mapper;
	}
}
