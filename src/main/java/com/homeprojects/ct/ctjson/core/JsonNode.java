package com.homeprojects.ct.ctjson.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class JsonNode implements JsonElement {

	private final List<JsonKeyValue> pairs = new ArrayList<>();

	public void addPair(JsonKeyValue pair) {
		pairs.add(pair);
	}
	
	public JsonElement getValue(String key) {
		return pairs.stream().filter(pair -> pair.getKey().equals(key)).findFirst().get().getValue();
	}
	
	public List<JsonKeyValue> getPairs() {
		return pairs;
	}

	@Override
	public String toString() {
		String keyvals = pairs.stream().map(pair -> pair.toString()).collect(Collectors.joining(", "));
		return "{" + keyvals + "}";
	}
	
}
