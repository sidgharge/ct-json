//package com.homeprojects.ct.ctjson2.core.deserializer;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import com.homeprojects.ct.ctjson2.core.Type;
//
//public class ListDeserializer extends Deserializer2<List> {
//	
//	@Override
//	protected <U> void add(List object, U arrayElement) {
//		object.add(arrayElement);
//	}
//
//	@Override
//	protected List initialize() {
//		return new ArrayList<>();
//	}
//
//	@Override
//	public Type getType() {
//		return new Type(List.class);
//	}
//
//	@Override
//	protected boolean isIterable() {
//		return true;
//	}
//
//}
