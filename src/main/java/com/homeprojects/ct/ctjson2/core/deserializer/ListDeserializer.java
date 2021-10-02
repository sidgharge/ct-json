package com.homeprojects.ct.ctjson2.core.deserializer;

import java.util.ArrayList;
import java.util.List;

import com.homeprojects.ct.ctjson2.core.JsonParser2;

public class ListDeserializer extends Deserializer<List> {

	@Override
	protected void setValue(List object, String key, JsonParser2 parser) {
		
	}

	@Override
	protected List initialize() {
		return new ArrayList<>();
	}

	@Override
	public Class<List> getType() {
		return List.class;
	}

	@Override
	protected boolean isIterable() {
		return true;
	}

}
