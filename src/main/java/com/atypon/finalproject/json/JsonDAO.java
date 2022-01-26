package com.atypon.finalproject.json;

import com.atypon.finalproject.users.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

@Repository
@Profile("database")
public class JsonDAO implements DAOInterface {
  @JsonProperty private static long JsonId = 0;

  private static final HashMap<String, JsonNode> DB2 = new HashMap<>();

  private static final HashMap<String, User> users = new HashMap<>();

  private static final JsonDAO dao = new JsonDAO();

  @Autowired
  private JsonDAO() {
    importDataAndClearExisting("DataBaseSchema.txt");
    User u = new User("admin", "admin");
    u.setAdmin(true);
    users.put("admin", u);
  }

  public static JsonDAO getInstance() {
    return dao;
  }

  @Override
  public User getUser(String user) {
    return users.get(user);
  }

  @Override
  public synchronized void addUsers(User user) {
    users.put(user.getUsername(), user);
    for (User u : users.values()) {
      System.out.println(u.getUsername());
    }
  }

  public boolean containUser(String username){
    return users.containsKey(username);
  }

  public static synchronized long generateId() {
    return JsonId++;
  }

  public static void resetId() {
    JsonId = 0;
  }

  @Override
  public synchronized void storeJson(String... jsons) {
    try {
      for (String j : jsons) {
        JsonNode node = Json.parseAndGenerateId(j);
        DB2.put(node.get("id").asText(), node);
      }
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      System.out.println("error during parsing the json ");
    }
  }

  @Override
  public synchronized void deleteJson(String id) {
    DB2.remove(id);
  }

  @Override
  public boolean containsJson(String id) {
    return DB2.containsKey(id);
  }

  @Override
  public synchronized void updateJson(String id, String json) {
    try {
      DB2.replace(id, Json.parse(json));
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      System.out.println("error during parsing the json ");
    }
  }
  // import/export database schema
  @Override
  public synchronized void exportDataBaseSchema(String path) {
    try {
      FileUtils.writeLines(new File(path), retrieveAll());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public synchronized void importDataAndClearExisting(String path) {
    try (Scanner scanner = new Scanner(new FileReader(path))) {
      DB2.clear();
      resetId();
      while (scanner.hasNextLine()) {
        String columns = scanner.nextLine();
        JsonNode node = Json.parse(columns);
        DB2.put(node.get("id").asText(), node);
        JsonId = Long.parseLong(node.get("id").asText());
      }
    } catch (Exception FileNotFoundException) {
      System.out.println("no such file exits");
      FileNotFoundException.printStackTrace();
    }
  }

  @Override
  public List<JsonNode> retrieveAll() {
    return new ArrayList<>(DB2.values());
  }

  public static JsonNode findById(String id) {
    return DB2.get(id);
  }
}
