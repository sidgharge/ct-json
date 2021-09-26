package com.homeprojects.ct.ctjson.core.deserializer;

import com.homeprojects.ct.ctjson.core.JsonValue;

public interface PrimitiveDeserializer {

	public static class ByteDeserializer implements PrimitiveDeserializer {

		public byte deserialize(JsonValue value) {
			return Byte.parseByte(value.getValue());
		}

	}

	public static class ShortDeserializer implements PrimitiveDeserializer {

		public short deserialize(JsonValue value) {
			return Short.parseShort(value.getValue());
		}

	}

	public static class IntDeserializer implements PrimitiveDeserializer {

		public int deserialize(JsonValue value) {
			return Integer.parseInt(value.getValue());
		}

	}
	
	public static class BooleanDeserializer implements PrimitiveDeserializer {

		public boolean deserialize(JsonValue value) {
			return Boolean.parseBoolean(value.getValue());
		}

	}
}
