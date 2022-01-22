package com.atypon.finalproject.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    ((ObjectNode) json).put("id", JsonDAO.generateId());
    return json;
  }

  public static JsonNode parse(String src) throws JsonProcessingException {
    JsonNode json =  objectMapper.readTree(src);
    return json;
  }

  public static <A> A fromJson(JsonNode node, Class<A> clazz) throws JsonProcessingException {
    return objectMapper.treeToValue(node, clazz);
  }

  public static String getJsonSchema(Class clazz) throws IOException {
    Field[] fields = clazz.getDeclaredFields();
    List<Map<String,String>> map=new ArrayList<Map<String,String>>();
    for (Field field : fields) {
      HashMap<String, String> objMap=new  HashMap<String, String>();
      objMap.put("", field.getName());
      objMap.put("type", field.getType().getSimpleName());
      objMap.put("format", "");
      map.add(objMap);
    }
    ObjectMapper mapper = new ObjectMapper();
    String json = mapper.writeValueAsString(map);

    return json;
  }

  public static JsonNode toJson(Object a) {
    return objectMapper.valueToTree(a);
  }

}
