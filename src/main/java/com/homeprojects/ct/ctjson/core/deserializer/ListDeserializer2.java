package com.homeprojects.ct.ctjson.core.deserializer;

import com.homeprojects.ct.ctjson.core.JsonMapper;
import com.homeprojects.ct.ctjson.core.RuntimeGenericTypeMetadata;
import com.homeprojects.ct.ctjson.core.parser.JsonParser;

import java.util.ArrayList;
import java.util.List;

public class ListDeserializer2 extends AbstractDeserializer<List> implements Deserializer<List> {

    @Override
    public List deserialize(String json) {
        throw new RuntimeException("Have not implemented JSON to List in ListDeserializer Class");
    }

    @Override
    public List deserialize(JsonParser parser) {
        return null;
    }

    @Override
    public List deserialize(String json, RuntimeGenericTypeMetadata metadata) {
        return null;
    }

    @Override
    public List deserialize(JsonParser parser, RuntimeGenericTypeMetadata metadata) {
        return parser.jsonArray(this, metadata);
    }

    @Override
    public List initialize() {
        return new ArrayList<>();
    }

    @Override
    public void setValue(List list, String key, JsonParser parser, RuntimeGenericTypeMetadata metadata) {
        Object object = mapper.deserialize(parser, metadata.getGeneric(0));
        list.add(object);
    }

    @Override
    public Class<List> getType() {
        return null;
    }

}
