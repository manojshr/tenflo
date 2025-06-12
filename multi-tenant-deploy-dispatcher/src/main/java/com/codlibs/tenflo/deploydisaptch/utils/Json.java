package com.codlibs.tenflo.deploydisaptch.utils;

import com.codlibs.tenflo.deploydisaptch.config.StaticContextAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Optional;

@JsonSerialize(using = JsonSerializer.class)
@JsonDeserialize(using = JsonDeserializer.class)
public class Json {

    private final JsonNode jsonNode;

    public Json(JsonNode jsonNode) {
        this.jsonNode = jsonNode;
    }

    public static Json json(String jsonString) {
        try {
            var mapper = StaticContextAccessor.getBean(ObjectMapper.class);
            return new Json(mapper.readTree(jsonString));
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
    }

    public Optional<String> field(String fieldName) {
        return Optional.ofNullable(jsonNode.get(fieldName)).map(JsonNode::asText);
    }
}
