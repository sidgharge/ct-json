//package com.homeprojects.ct.ctjson2.core.deserializer;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import com.homeprojects.ct.ctjson2.core.JsonParser2;
//import com.homeprojects.ct.ctjson2.core.Type;
//
//public class MapDeserializer extends Deserializer2<Map> {
//
//	@Override
//	protected void setValue(Map object, String key, JsonParser2 parser) {
//		object.put(key, parser.getNextGenericElement());
//	}
//
//	@Override
//	protected Map initialize() {
//		return new HashMap();
//	}
//
//	@Override
//	public Type getType() {
//		return new Type(Map.class);
//	}
//
//	@Override
//	protected boolean isIterable() {
//		return false;
//	}
//
//}