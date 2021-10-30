package com.homeprojects.ct.ctjson.core.parser;

import com.homeprojects.ct.ctjson.core.JsonArray;
import com.homeprojects.ct.ctjson.core.JsonElement;
import com.homeprojects.ct.ctjson.core.JsonKeyValue;
import com.homeprojects.ct.ctjson.core.JsonNode;
import com.homeprojects.ct.ctjson.core.JsonValue;
import com.homeprojects.ct.ctjson.core.JsonValueType;
import com.homeprojects.ct.ctjson.core.deserializer.Deserializer;

public class JsonParser<T> {

	private int i = 0;

	private final String json;
	
	private Deserializer<T> deserializer;
	
	public JsonParser(String json, Deserializer<T> deserializer) {
		this.json = json;
	}

	public T parse() {
		skipWhiteSpace();
		if(i >= json.length()) {
			return null;
		}
		
		char c = getCharacter();
		if (c == '{') {
			return startJsonObject();
		} 
//		else if(c == '[') {
//			return startJsonArray();
//		}
		
		return null; // TODO throw exception
	}

//	private JsonElement startJsonArray() {
//		JsonArray array = new JsonArray();
//		
//		skipWhiteSpace();
//		char c = getCharacter();
//		
//		if(c != '[') {
//			// TODO throw exception
//		}
//		i++;
//		
//		c = getCharacter();
//		
//		while(c != ']' && i < json.length()) {
//			JsonElement element = getJsonElement();
//			array.addElement(element);
//			skipComma();
//			c = getCharacter();
//		}
//		i++;
//		return array;
//	}
//
//	private JsonElement getJsonElement() {
//		skipWhiteSpace();
//		char c = getCharacter();
//		if (c == '{') {
//			return startJsonObject();
//		} else if(c == '[') {
//			return startJsonArray();
//		} else if(c == '"') {
//			i++;
//			return new JsonValue(getNextKey(), JsonValueType.STRING);
//		} else if(Character.isDigit(c)) {
//			return new JsonValue(getNextNumber(), JsonValueType.NUMBER);
//		} else {
//			JsonValue booleanValue = getBoolean();
//			if(booleanValue != null) {
//				return booleanValue;
//			} else {
//				// TODO throw an error
//				return null;
//			}
//		}
//	}

	private T startJsonObject() {
		T object = deserializer.initialize();
		
		skipWhiteSpace();
		char c = getCharacter();
		
		if(c != '{') {
			// TODO throw exception
		}
		i++;
		
		c = getCharacter();
		while(c != '}' && i < json.length()) {
			setJsonKeyValuePair(object);
			skipComma();
			c = getCharacter();
		}
		i++;
		
		return object;
	}

	private char getCharacter() {
		return json.charAt(i);
	}

	private void skipComma() {
		while (i < json.length()) {
			if(getCharacter() == ',') {
				i++;
				break;
			}
			if(getCharacter() == '}') {
				break;
			}
			if(getCharacter() == ']') {
				break;
			}
			i++;
		}
	}

	private void setJsonKeyValuePair(T object) {
		skipWhiteSpace();
		char c = getCharacter();
		
		if(c != '"') {
			// TODO throw exception
		}
		i++;
		
		String key = getNextKey();
		
		skipWhiteSpace();
		c = getCharacter();
		if(c != ':') {
			// TODO throw exception
		}
		i++;
		
		skipWhiteSpace();
		c = getCharacter();
		if(c == '{') { // nested json
			// pair.setValue(startJsonObject()); // TODO Nested JSON
		} else if(Character.isDigit(c)) {
			String nextNumber = getNextNumber();
			deserializer.setValue(key, nextNumber);
		} else if(c == '"') { // string value
			i++;
			String str = getNextKey();
			deserializer.setValue(key, str);
		}
//		else if(c == '[') { // TODO Array
//			JsonElement element = startJsonArray();
//			pair.setValue(element);
//		}
		else { // boolean or null
			JsonValue booleanValue = getBoolean();
			if(booleanValue != null) {
				deserializer.setValue(key, booleanValue);
			} else {
				// TODO throw an error
			}
		}
		// TODO null as value
	}

	private String getNextNumber() {
		StringBuilder builder = new StringBuilder();
		char c = getCharacter();
		while (i < json.length() && (Character.isDigit(c) || c == '.')) {
			builder.append(c);
			i++;
			c = getCharacter();
		}
		return builder.toString();
	}

	private String getNextKey() {
		StringBuilder builder = new StringBuilder();
		char c;
		while (i < json.length() && (c = json.charAt(i++)) != '"') {
			builder.append(c);
		}
		return builder.toString();
	}
	
	private JsonValue getBoolean() {
		if(i + 3 < json.length()) {
			String b = json.substring(i, i + 4);
			if(b.equals("true")) {
				i = i + 4;
				return new JsonValue(b, JsonValueType.BOOLEAN);
			}	
		}
		if(i + 4 < json.length()) {
			String b = json.substring(i, i + 5);
			if(b.equals("false")) {
				i = i + 5;
				return new JsonValue(b, JsonValueType.BOOLEAN);
			}	
		}
		return null;
	}
	
	private void skipWhiteSpace() {
		char c = getCharacter();
		while (Character.isWhitespace(c) && i < json.length()) {
			i++;
			c = getCharacter();
		}
	}
}
