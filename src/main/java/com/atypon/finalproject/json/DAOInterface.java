package com.atypon.finalproject.json;

import com.atypon.finalproject.users.User;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

public interface DAOInterface {


  User getUser(String user);

  void addUsers(User user);

  void storeJson(String... jsons);

  void deleteJson(String id);

  boolean containsJson(String id);

  void updateJson(String id, String json);

  void exportDataBaseSchema(String path);

  void importDataAndClearExisting(String path);

  List<JsonNode> retrieveAll();
}
