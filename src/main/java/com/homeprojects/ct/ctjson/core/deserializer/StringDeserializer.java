package com.homeprojects.ct.ctjson.core.deserializer;

public class StringDeserializer extends AbstractDeserializer<String> {

	@Override
	public String deserialize(String value) {
		return value;
	}

	@Override
	public Class<String> getType() {
		return String.class;
	}

	@Override
	public String initialize() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <U> void setValue(String fieldName, U value) {
		// TODO Auto-generated method stub
		
	}
}
