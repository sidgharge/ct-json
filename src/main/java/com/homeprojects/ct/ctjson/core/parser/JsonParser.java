package com.homeprojects.ct.ctjson.core.parser;

import com.homeprojects.ct.ctjson.core.JsonArray;
import com.homeprojects.ct.ctjson.core.JsonElement;
import com.homeprojects.ct.ctjson.core.JsonKeyValue;
import com.homeprojects.ct.ctjson.core.JsonNode;
import com.homeprojects.ct.ctjson.core.JsonValue;
import com.homeprojects.ct.ctjson.core.JsonValueType;

public class JsonParser {

	private int i = 0;

	private final String json;
	
	public JsonParser(String json) {
		this.json = json;
	}

	public JsonElement parse() {
		skipWhiteSpace();
		if(i >= json.length()) {
			return null;
		}
		
		char c = getCharacter();
		if (c == '{') {
			return startJsonObject();
		} else if(c == '[') {
			return startJsonArray();
		}
		
		return null; // TODO throw exception
	}

	private JsonElement startJsonArray() {
		JsonArray array = new JsonArray();
		
		skipWhiteSpace();
		char c = getCharacter();
		
		if(c != '[') {
			// TODO throw exception
		}
		i++;
		
		c = getCharacter();
		
		while(c != ']' && i < json.length()) {
			JsonElement element = getJsonElement();
			array.addElement(element);
			skipComma();
			c = getCharacter();
		}
		i++;
		return array;
	}

	private JsonElement getJsonElement() {
		skipWhiteSpace();
		char c = getCharacter();
		if (c == '{') {
			return startJsonObject();
		} else if(c == '[') {
			return startJsonArray();
		} else if(c == '"') {
			i++;
			return new JsonValue(getNextKey(), JsonValueType.STRING);
		} else if(Character.isDigit(c)) {
			return new JsonValue(getNextNumber(), JsonValueType.NUMBER);
		} else {
			JsonValue booleanValue = getBoolean();
			if(booleanValue != null) {
				return booleanValue;
			} else {
				// TODO throw an error
				return null;
			}
		}
	}

	private JsonNode startJsonObject() {
		JsonNode node = new JsonNode();
		
		skipWhiteSpace();
		char c = getCharacter();
		
		if(c != '{') {
			// TODO throw exception
		}
		i++;
		
		c = getCharacter();
		while(c != '}' && i < json.length()) {
			JsonKeyValue pair = getJsonKeyValuePair();
			node.addPair(pair);
			skipComma();
			c = getCharacter();
		}
		i++;
		
		return node;
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

	private JsonKeyValue getJsonKeyValuePair() {
		skipWhiteSpace();
		char c = getCharacter();
		
		if(c != '"') {
			// TODO throw exception
		}
		i++;
		
		JsonKeyValue pair = new JsonKeyValue();
		String key = getNextKey();
		pair.setKey(key);
		
		skipWhiteSpace();
		c = getCharacter();
		if(c != ':') {
			// TODO throw exception
		}
		i++;
		
		skipWhiteSpace();
		c = getCharacter();
		if(c == '{') {
			pair.setValue(startJsonObject());
		} else if(Character.isDigit(c)) {
			String nextNumber = getNextNumber();
			JsonValue value = new JsonValue(nextNumber, JsonValueType.NUMBER);
			pair.setValue(value);
		} else if(c == '"') {
			i++;
			String str = getNextKey();
			JsonValue value = new JsonValue(str, JsonValueType.STRING);
			pair.setValue(value);
		} else if(c == '[') {
			JsonElement element = startJsonArray();
			pair.setValue(element);
		} else {
			JsonValue booleanValue = getBoolean();
			if(booleanValue != null) {
				pair.setValue(booleanValue);
			} else {
				// TODO throw an error
			}
		}
		// TODO null as value
		return pair;
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
