package com.atypon.finalproject.database;

import com.atypon.finalproject.utility.Json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import static com.atypon.finalproject.database.IDGenerator.*;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

@Repository
@Profile("database")
public class DocumentDAO implements BasicDAOInterface {

  protected static final HashMap<String, JsonNode> DB = new HashMap<>();

  private static final DocumentDAO dao = new DocumentDAO();

  @Autowired
  private DocumentDAO() {
    initialDataBaseLoading();
  }

  private synchronized void initialDataBaseLoading() {
    try (Scanner scanner = new Scanner(new FileReader("DataBaseSchema.txt"))) {
      DB.clear();
      resetId();
      while (scanner.hasNextLine()) {
        String columns = scanner.nextLine();
        JsonNode node = Json.parse(columns);
        DB.put(node.get("id").asText(), node);
        jsonId = Long.parseLong(node.get("id").asText());
      }
    } catch (IOException fileNotFoundException) {
      System.out.println("File not found during initialization,no such file exits");
      fileNotFoundException.printStackTrace();
    }
  }

  public static DocumentDAO getInstance() {
    return dao;
  }

  @Override
  public synchronized void storeJson(String... jsons) {
    try {
      for (String j : jsons) {
        JsonNode node = Json.parseAndGenerateId(j);
        DB.put(node.get("id").asText(), node);
      }
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      System.out.println("error during parsing the json ");
    }
  }

  @Override
  public synchronized void deleteJson(String id) {
    DB.remove(id);
  }

  @Override
  public boolean containsJson(String id) {
    return DB.containsKey(id);
  }

  @Override
  public synchronized void updateJson(String id, String json) {
    try {
      DB.replace(id, Json.parse(json));
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      System.out.println("error during parsing the json ");
    }
  }

  public static List<JsonNode> retrieveAll() {
    return new ArrayList<>(DB.values());
  }
}
