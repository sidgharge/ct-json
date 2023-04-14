package com.homeprojects.ct.ctjson.core.deserializer;

import com.homeprojects.ct.ctjson.core.JsonMapper;
import com.homeprojects.ct.ctjson.core.RuntimeGenericTypeMetadata;
import com.homeprojects.ct.ctjson.core.parser.JsonParser;

import java.util.List;

public class ListDeserializer2 implements Deserializer<List> {

    @Override
    public List deserialize(String json) {
        throw new RuntimeException("Have not implemented JSON to List in ListDeserializer Class");
    }

    @Override
    public List deserialize(JsonParser parser) {
//        return parser.;
        return null;
    }

    @Override
    public List deserialize(String json, RuntimeGenericTypeMetadata metadata) {
        return null;
    }

    @Override
    public List deserialize(JsonParser parser, RuntimeGenericTypeMetadata metadata) {
        return null;
    }

    @Override
    public List initialize() {
        return null;
    }

    @Override
    public void setValue(List object, String key, JsonParser parser, RuntimeGenericTypeMetadata metadata) {

    }

    @Override
    public Class<List> getType() {
        return null;
    }

    @Override
    public void setJsonMapper(JsonMapper mapper) {

    }
}
