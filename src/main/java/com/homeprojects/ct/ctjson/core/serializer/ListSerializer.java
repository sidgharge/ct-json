package com.homeprojects.ct.ctjson.core.serializer;

import java.util.List;

import com.homeprojects.ct.ctjson.core.JsonArray;
import com.homeprojects.ct.ctjson.core.JsonElement;

@SuppressWarnings("rawtypes")
public class ListSerializer extends AbstractSerializer<Iterable> {

	@Override
	public JsonElement serialize(Iterable list) {
		JsonArray array = new JsonArray();
		for (Object object : list) {
			JsonElement element = mapper.serialize(object);
			array.addElement(element);
		}
		return array;
	}

	@Override
	public Class getType() {
		return List.class;
	}

}
