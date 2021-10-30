package com.homeprojects.ct.ctjson2.core;

import com.homeprojects.ct.ctjson2.core.deserializer.Deserializer2;

@SuppressWarnings("rawtypes")
public class JsonParser2 {

	private String json;

	private int i;
	
//	private JsonMapper2 mapper;
	
	private Type type;

	public JsonParser2(String json, JsonMapper2 mapper, Type type) {
		this.json = json;
		this.i = 0;
//		this.mapper = mapper;
		this.type = type;
	}
	
	private JsonParser2(String json, JsonMapper2 mapper, Type type, int i) {
		this.json = json;
		this.i = i;
//		this.mapper = mapper;
		this.type = type;
	}

	public char getNextCharacter() {
		i++;
		skipWhiteSpace();
		return getCharacter();
	}

	public char getCharacter() {
		return json.charAt(i);
	}
	
	public void decrement() {
		i--;
	}
	
	public void increment() {
		i++;
	}
	
	public String getNextString() {
		i++;
		StringBuilder builder = new StringBuilder();
		char c;
		while (i < json.length() && (c = json.charAt(i++)) != '"') {
			builder.append(c);
		}
		return builder.toString();
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
	
	public boolean getNextBoolean() {
		char c = getCharacter();
		if(c == 't' && (i + 3) < json.length() && json.substring(i, i + 4).equals("true")) {
			i = i + 4;
			return true;
		}
		if(c == 'f'  && (i + 4) < json.length() && json.substring(i, i + 5).equals("false")) {
			i = i + 5;
			return false;
		}
		// TODO throw exception
		return false;
	}
	
	public int toInt(String number) {
		return Integer.parseInt(number);
	}
	
	public void skipWhiteSpace() {
		char c = getCharacter();
		while (Character.isWhitespace(c) && i < json.length()) {
			i++;
			c = getCharacter();
		}
	}
	
	public void skipComma() {
		skipWhiteSpace();
		if(getCharacter() == ',') {
			skipWhiteSpace();
			i++;
		}
	}
	
	public void skipValue() {
		char c = getCharacter();
		if(c == '{') {
			skipObject();
		} else if(c == '[') {
			skipArray();
		} else {
			skipToNextComma();
		}
	}

	private void skipToNextComma() {
		char c = getCharacter();
		if(c != ',') {
			c = getNextCharacter();
		}
		getNextCharacter();
	}

	private void skipObject() {
		char c = getCharacter();
		while(c != '}') {
			c = getNextCharacter();
			if(c == '{') {
				skipObject();
			} else if(c == '[') {
				skipArray();
			}
			c = getCharacter();
		}
		getNextCharacter();
	}

	private void skipArray() {
		char c = getCharacter();
		while(c != ']') {
			c = getNextCharacter();
			if(c == '{') {
				skipObject();
			} else if(c == '[') {
				skipArray();
			}
			c = getCharacter();
		}
		getNextCharacter();   
	}
	
	public Type getType() {
		return type;
	}
}
