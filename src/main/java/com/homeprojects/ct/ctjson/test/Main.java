package com.homeprojects.ct.ctjson.test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.homeprojects.ct.ctjson.core.JsonElement;
import com.homeprojects.ct.ctjson.core.JsonMapper;
import com.homeprojects.ct.ctjson.core.parser.JsonParser;

public class Main {

	public static void main(String[] args) throws IOException {
		String json = new String(Files.readAllBytes(Paths.get("/home/sid/Sid/Workspaces/compiletime-code-generation/ct-json/src/main/java/com/homeprojects/ct/ctjson/test/user.json")));

		JsonParser parser = new JsonParser(json);
		JsonElement element = parser.parse();
		
		JsonMapper mapper = new JsonMapper();
		new UserDeserilizer(mapper);
		
		System.out.println(mapper.deserialize(element, User.class));
	}
}
