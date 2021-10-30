//package com.homeprojects.ct.ctjson.core.deserializer;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import com.homeprojects.ct.ctjson.core.JsonArray;
//import com.homeprojects.ct.ctjson.core.JsonElement;
//
//public class ListDeserializer implements ArrayDeserializer<List<?>> {
//
//	@SuppressWarnings({ "rawtypes", "unchecked" })
//	@Override
//	public List<?> deserialize(JsonArray array, Deserializer<JsonElement, ?> deserializer) {
//		List list = new ArrayList<>();
//		for (JsonElement element : array.getElements()) {
//			list.add(deserializer.deserialize(element));
//		}
//		return list;
//	}
//
////	public <T> List<T> deserialize(JsonArray element, Class<T> clazz) {
////		return null;
////	}
//
//}
