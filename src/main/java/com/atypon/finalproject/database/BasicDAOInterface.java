package com.atypon.finalproject.database;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

public interface BasicDAOInterface {

  JsonNode findById(String id);

  void storeJson(String... jsons);

  void deleteJson(String id);

  boolean containsJson(String id);

  void updateJson(String id, String json);


}
