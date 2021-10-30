package com.homeprojects.ct.ctjson2.core;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@SuppressWarnings("rawtypes")
public class Type {

	private Class clazz;

	private Map<String, Type> parameters;

	public Type(Class clazz, Map<String, Type> parameters) {
		this.clazz = clazz;
		this.parameters = parameters;
	}

	public Type(Class clazz) {
		this(clazz, Collections.emptyMap());
	}

	public Class getClazz() {
		return clazz;
	}

	public Map<String, Type> getParameters() {
		return parameters;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((clazz == null) ? 0 : clazz.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Type other = (Type) obj;
		if (clazz == null) {
			if (other.clazz != null)
				return false;
		} else if (!clazz.equals(other.clazz))
			return false;
		return true;
	}

	public static void main(String[] args) {
//		Type type = new Type(List.class, new Type(Integer.class));
//		Class clazz2 = type.clazz;
//		for(Type p: type.parameters) {
//			p.clazz
//			p.type
//		}
	}
}
