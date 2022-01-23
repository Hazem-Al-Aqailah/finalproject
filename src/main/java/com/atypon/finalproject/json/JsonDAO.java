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
public class JsonDAO {
  @JsonProperty private static long JsonId = 0;

  private static final HashMap<String, JsonNode> DB2 = new HashMap<>();

  private static final HashMap<String, User> users = new HashMap<>();

  private static final JsonDAO dao = new JsonDAO();

  @Autowired
  private JsonDAO(){
    importDataAndClearExisting("DataBaseSchema.txt");
  }

  public static JsonDAO getInstance(){
    return dao;
  }

  public User getUser (String user){
    return users.get(user);
  }

  public synchronized void addUsers(User... user){
    for (User u:user){
      users.put(u.getUsername(),u);
    }
  }

  public synchronized static long generateId() {
    return JsonId++;
  }

  public static void resetId() {
    JsonId = 0;
  }

  public synchronized void storeJson(String... jsons) throws JsonProcessingException {
    for (String j : jsons) {
      JsonNode node = Json.parseAndGenerateId(j);
      DB2.put(node.get("id").asText(), node);
    }
  }

  public synchronized void deleteJson(String id){
    DB2.remove(id);
  }

  public boolean containsJson(String id){return DB2.containsKey(id);}

  public synchronized void  updateJson(String id, String json) throws JsonProcessingException {
    DB2.replace(id, Json.parse(json));
  }

  // import/export database schema
  public synchronized void exportDataBaseSchema(String path) throws IOException {
    FileUtils.writeLines(new File(path), retrieveAll());
  }

  public synchronized void importDataAndClearExisting(String path) {
    try(Scanner scanner = new Scanner(new FileReader(path))) {
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
      System.out.println(FileNotFoundException);
    }
  }

  public List<JsonNode> retrieveAll() {
    return new ArrayList<>(DB2.values());
  }

  public static JsonNode findById(String id) {
    return DB2.get(id);
  }

}
