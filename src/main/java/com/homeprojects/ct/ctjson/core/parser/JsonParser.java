package com.homeprojects.ct.ctjson.core.parser;

import com.homeprojects.ct.ctjson.core.RuntimeGenericTypeMetadata;
import com.homeprojects.ct.ctjson.core.deserializer.Deserializer;

public class JsonParser {

	private int i = 0;

	private final String json;
	
	public JsonParser(String json) {
		this.json = json;
	}

	public <T> T parse(Deserializer<T> deserializer, RuntimeGenericTypeMetadata metadata) {
		skipWhiteSpace();
		if(i >= json.length()) {
			return null;
		}
		
		char c = getCharacter();
		if (c == '{') {
			return startJsonObject(deserializer, metadata);
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

	private <T> T startJsonObject(Deserializer<T> deserializer, RuntimeGenericTypeMetadata metadata) {
		T object = deserializer.initialize();
		
		skipWhiteSpace();
		char c = getCharacter();
		
		if(c != '{') {
			// TODO throw exception
		}
		i++;
		
		c = getCharacter();
		while(c != '}' && i < json.length()) {
			setJsonKeyValuePair(object, deserializer, metadata);
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

	private <T> void setJsonKeyValuePair(T object, Deserializer<T> deserializer, RuntimeGenericTypeMetadata metadata) {
		skipWhiteSpace();
		char c = getCharacter();
		
		if(c != '"') {
			// TODO throw exception
		}
		String key = getNextKey();
		
		skipWhiteSpace();
		c = getCharacter();
		if(c != ':') {
			// TODO throw exception
		}
		i++;
		
		skipWhiteSpace();
		deserializer.setValue(object, key, this, metadata);
	}

	public String getNextNumber() {
		StringBuilder builder = new StringBuilder();
		char c = getCharacter();
		while (i < json.length() && (Character.isDigit(c) || c == '.')) {
			builder.append(c);
			i++;
			c = getCharacter();
		}
		return builder.toString();
	}

	public String getNextKey() {
		StringBuilder builder = new StringBuilder();
		char c;
		i++;
		while (i < json.length() && (c = json.charAt(i++)) != '"') {
			builder.append(c);
		}
		return builder.toString();
	}
	
	public String getNextString() {
		return getNextKey();
	}
	
	private Boolean getNextBoolean() {
		if(i + 3 < json.length()) {
			String b = json.substring(i, i + 4);
			if(b.equals("true")) {
				i = i + 4;
				return true;
			}	
		}
		if(i + 4 < json.length()) {
			String b = json.substring(i, i + 5);
			if(b.equals("false")) {
				i = i + 5;
				return false;
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
	
	// Utility methods
	public int toInt(Object object) {
		return Integer.parseInt(object.toString());
	}
	
	public short toShort(Object object) {
		return Short.parseShort(object.toString());
	}
	
	public byte toByte(Object object) {
		return Byte.parseByte(object.toString());
	}
	
	public long toLong(Object object) {
		return Long.parseLong(object.toString());
	}
	
	public double toDouble(Object object) {
		return Double.parseDouble(object.toString());
	}
	
	public float toFloat(Object object) {
		return Float.parseFloat(object.toString());
	}
}
