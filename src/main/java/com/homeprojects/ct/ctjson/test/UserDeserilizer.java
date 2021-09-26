package com.homeprojects.ct.ctjson.test;

import java.util.List;

import com.homeprojects.ct.ctjson.core.JsonElement;
import com.homeprojects.ct.ctjson.core.JsonMapper;
import com.homeprojects.ct.ctjson.core.JsonNode;
import com.homeprojects.ct.ctjson.core.deserializer.Deserializer;

public class UserDeserilizer implements Deserializer<JsonElement, User> {
	
	private final JsonMapper mapper;
	
	public UserDeserilizer(JsonMapper mapper) {
		this.mapper = mapper;
		mapper.registerDeserializer(User.class, this);
	}

	@Override
	public User deserialize(JsonElement element) {
		JsonNode node = (JsonNode) element;
		
		User user = new User();
		user.setName(mapper.deserialize(node.getValue("name"), String.class));
		user.setAge(mapper.deserializeAsInt(node.getValue("age")));
		user.setActive(mapper.deserializeAsBoolean(node.getValue("isActive")));
		
		List<Integer> favNums = mapper.deserializeArray(node.getValue("favNums"), List.class, Integer.class);
//		user.setFavNums(favNums);
		return user;
	}

	

}
