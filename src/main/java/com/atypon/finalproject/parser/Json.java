package com.atypon.finalproject.parser;

import com.atypon.finalproject.database.DocumentDAO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class Json {

  private static ObjectMapper objectMapper = getDefaultObjectMapper();

  private static ObjectMapper getDefaultObjectMapper() {
    ObjectMapper defaultObjectMapper = new ObjectMapper();
    // to not fail when new properties are created
    defaultObjectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    return defaultObjectMapper;
  }

  public static JsonNode parseAndGenerateId(String src) throws JsonProcessingException {
    JsonNode json =  objectMapper.readTree(src);
    ((ObjectNode) json).put("id", DocumentDAO.generateId());
    return json;
  }

  public static JsonNode parse(String src) throws JsonProcessingException {
    JsonNode json =  objectMapper.readTree(src);
    return json;
  }

}
