package com.homeprojects.ct.ctjson.core.serializer;

import com.homeprojects.ct.ctjson.core.JsonArray;
import com.homeprojects.ct.ctjson.core.JsonElement;
import com.homeprojects.ct.ctjson.core.JsonKeyValue;
import com.homeprojects.ct.ctjson.core.JsonNode;
import com.homeprojects.ct.ctjson.core.JsonValue;
import com.homeprojects.ct.ctjson.core.JsonValueType;

public class ObjectParser {

	public String parse(JsonElement element) {
		StringBuilder builder = new StringBuilder();
		visitJsonElement(element, builder);
		return builder.toString();
	}

	private void startJsonObject(JsonNode node, StringBuilder builder) {
		builder.append('{');
		iterateJsonKeys(node, builder);
		builder.append('}');
	}

	private void iterateJsonKeys(JsonNode node, StringBuilder builder) {
		for(JsonKeyValue pair: node.getPairs()) {
			builder.append(appendQuotes(pair.getKey()));
			builder.append(':');
			visitJsonElement(pair.getValue(), builder);
			builder.append(',');
		}
		deleteLastCharacter(builder); // To delete last comma
	}

	private void deleteLastCharacter(StringBuilder builder) {
		builder.deleteCharAt(builder.length() - 1);
	}
	
	private void visitJsonElement(JsonElement element, StringBuilder builder) {
		if(element instanceof JsonNode) {
			startJsonObject((JsonNode) element, builder);
		} else if(element instanceof JsonValue) {
			visitJsonValue((JsonValue) element, builder);
		} else if(element instanceof JsonArray) {
			visitJsonArray((JsonArray)element, builder);
		}
	}
	
	private void visitJsonArray(JsonArray array, StringBuilder builder) {
		builder.append('[');
		visitJsonArrayElement(array, builder);
		builder.append(']');
	}

	private void visitJsonArrayElement(JsonArray array, StringBuilder builder) {
		for (JsonElement element : array.getElements()) {
			visitJsonElement(element, builder);
			builder.append(',');
		}
		deleteLastCharacter(builder); // To delete last comma
	}

	private void visitJsonValue(JsonValue value, StringBuilder builder) {
		if(value.getType().equals(JsonValueType.STRING)) {
			builder.append(appendQuotes(value.getValue()));
		} else {
			builder.append(value.getValue());
		}
	}

	private String appendQuotes(String str) {
		return "\"" + str + "\"";
	}
}
