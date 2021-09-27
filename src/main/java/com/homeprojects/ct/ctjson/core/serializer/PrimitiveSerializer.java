package com.homeprojects.ct.ctjson.core.serializer;

import com.homeprojects.ct.ctjson.core.JsonValue;
import com.homeprojects.ct.ctjson.core.JsonValueType;

public interface PrimitiveSerializer {

	public static class Byteserializer implements PrimitiveSerializer {

		public JsonValue serialize(byte value) {
			return new JsonValue(Byte.toString(value), JsonValueType.NUMBER);
		}

	}

	public static class Shortserializer implements PrimitiveSerializer {

		public JsonValue serialize(short value) {
			return new JsonValue(Short.toString(value), JsonValueType.NUMBER);
		}

	}

	public static class Intserializer implements PrimitiveSerializer {

		public JsonValue serialize(int value) {
			return new JsonValue(Integer.toString(value), JsonValueType.NUMBER);
		}

	}
	
	public static class Booleanserializer implements PrimitiveSerializer {

		public JsonValue serialize(boolean value) {
			return new JsonValue(Boolean.toString(value), JsonValueType.BOOLEAN);
		}

	}
}
