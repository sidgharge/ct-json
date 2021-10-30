package com.homeprojects.ct.ctjson2.core.deserializer;

import com.homeprojects.ct.ctjson2.core.JsonMapper2;
import com.homeprojects.ct.ctjson2.core.JsonParser2;
import com.homeprojects.ct.ctjson2.core.Type;

public abstract class Deserializer2<T> {
	
	protected JsonMapper2 mapper;

	public T deserialize(String json, Type type) {
		JsonParser2 parser = new JsonParser2(json, mapper, type);
		return deserialize(parser);
	}
	
	public T deserialize(JsonParser2 parser) {
		skipWhiteSpace(parser);
		char c = getCharacter(parser);
		
		if ((isIterable() && c == '{') || (!isIterable() && c == '[')) {
			// TODO Handle throw exception
		}
		if(isIterable()) {
			return startJsonArray(parser);
		} else {
			return startJsonObject(parser);
		}
	}
	
	@SuppressWarnings("unchecked")
	protected <U> U deserializeInnerObject(JsonParser2 parser, Class<U> clazz) {
		return (U) mapper.getDeserializer(new Type(clazz)).deserialize(parser);
	}
	
	protected Object deserializeInnerGenericObject(String type, JsonParser2 parser) {
		return mapper.getDeserializer(parser.getType().getParameters().get(type)).deserialize(parser);
	}

	protected char getCharacter(JsonParser2 parser) {
		return parser.getCharacter();
	}

	protected void skipWhiteSpace(JsonParser2 parser) {
		parser.skipWhiteSpace();
	}
	
	private T startJsonArray(JsonParser2 parser) {
		T object = initialize();
		
		char c = getCharacter(parser);
		while(c != ']') {
			Object arrayElement = getNextGenericElement(parser);
			add(object, arrayElement);
			c = getCharacter(parser);
			parser.decrement();
		}
		parser.increment();
		return object;
	}
	
	protected <U> void add(T object, U arrayElement) {
		
	}

	private Object getNextGenericElement(JsonParser2 parser) {
		getNextCharacter(parser);
	//	return parser.deserializeGenericObject();
		return null; // TODO Handle
	}

	protected char getNextCharacter(JsonParser2 parser) {
		return parser.getNextCharacter();
	}

	private T startJsonObject(JsonParser2 parser) {
		T object = initialize();
		
		char c = getCharacter(parser);
		while(c != '}') {
			setJsonKeyValuePair(object, parser);
			c = getCharacter(parser);
		}
		parser.increment();
		return object;
	}
	
	private void setJsonKeyValuePair(T object, JsonParser2 parser) {
		char c = getNextCharacter(parser);
		if(c != '"') {
			// TODO throw exception
		}
		String key = getNextString(parser);
		skipWhiteSpace(parser);
		c = getCharacter(parser);
		if(c != ':') {
			// TODO throw exception
		}
		c = getNextCharacter(parser);
		setValue(object, key, parser);
		skipComma(parser);
	}

	protected void skipComma(JsonParser2 parser) {
		parser.skipComma();
	}

	protected String getNextString(JsonParser2 parser) {
		return parser.getNextString();
	}
	
	protected String getNextNumber(JsonParser2 parser) {
		return parser.getNextNumber();
	}
	
	protected void setValue(T object, String key, JsonParser2 parser) {}

	protected abstract T initialize();
	
	public abstract Type getType();
	
	protected abstract boolean isIterable();
	
	public void setMapper(JsonMapper2 mapper) {
		this.mapper = mapper;
	}
}
