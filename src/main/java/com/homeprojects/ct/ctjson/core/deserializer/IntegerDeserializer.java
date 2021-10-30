package com.homeprojects.ct.ctjson.core.deserializer;

public class IntegerDeserializer extends AbstractDeserializer<Integer> {

	@Override
	public Integer deserialize(String str) {
		return Integer.valueOf(str);
	}

	@Override
	public Class<Integer> getType() {
		return Integer.class;
	}

	@Override
	public Integer initialize() {
		return null;
	}

	@Override
	public <U> void setValue(String fieldName, U value) {
		
	}
}
