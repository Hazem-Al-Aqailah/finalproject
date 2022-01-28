package com.atypon.finalproject.database;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

public interface BasicDAOInterface {

  JsonNode findById(String id);

  void storeJson(String... jsons);

  void deleteJson(String id);

  boolean containsJson(String id);

  void updateJson(String id, String json);

  void exportDataBaseSchema(String path);

  void importDataAndClearExisting(String path);

  List<JsonNode> retrieveAll();
}
