package com.atypon.finalproject.database;

public interface BasicDAOInterface {

  void storeJson(String... jsons);

  void deleteJson(String id);

  boolean containsJson(String id);

  void updateJson(String id, String json);
}
