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
  @JsonProperty private static long generatedId = 0;

  private static final HashMap<String, JsonNode> DB2 = new HashMap<>();

  private static final HashMap<String, ArrayList<String>> schemaIndex = new HashMap<>();

  private static final HashMap<String, ArrayList<String>> nameIndex = new HashMap<>();

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

  public void addUsers(User... user){
    for (User u:user){
      users.put(u.getUsername(),u);
    }
  }

  public static long generateId() {
    return generatedId++;
  }

  public static void resetId() {
    generatedId = 0;
  }

  public void storeJson(JsonNode... jsons) {
    for (JsonNode j : jsons) {
      DB2.put(j.get("id").asText(), j);
    }
    indexName();
  }

  public void storeJson(String... jsons) throws JsonProcessingException {
    for (String j : jsons) {
      JsonNode node = Json.parseAndGenerateId(j);
      DB2.put(node.get("id").asText(), node);
    }
    indexName();
  }

  public void deleteJson(String id){
    DB2.remove(id);
  }

  public boolean containsJson(String id){return DB2.containsKey(id);};

  public void updateJson(String id, String json) throws JsonProcessingException {
    DB2.replace(id, Json.parse(json));
  }

  //indexing by name
  private static void indexName() {
    nameIndex.clear();
    for (JsonNode j : DB2.values()) {
      String name = j.get("author").asText();
      String id = j.get("id").asText();
      if (nameIndex.containsKey(name)) {
        nameIndex.get(name).add(id);
      }
      JsonDAO.addToListOfIndexedProperty(name, id,nameIndex);
    }
  }

  private static void indexSchema(){
    schemaIndex.clear();
    for (JsonNode j : DB2.values()) {
      String schema = j.get("schema").asText();
      String id = j.get("id").asText();
      if (schemaIndex.containsKey(schema)) {
        schemaIndex.get(schema).add(id);
      }
      JsonDAO.addToListOfIndexedProperty(schema, id,schemaIndex);
    }
  }
  //used in indexName method
  private static void addToListOfIndexedProperty(String indexedValue, String item, HashMap<String, ArrayList<String>> indexedMap) {
    ArrayList<String> itemsList = indexedMap.get(indexedValue);
    // if list does not exist create it
    if (itemsList == null) {
      itemsList = new ArrayList<String>();
      itemsList.add(item);
      indexedMap.put(indexedValue, itemsList);
    } else {
      // add if item is not already in list
      if (!itemsList.contains(item)) itemsList.add(item);
    }
  }

  public List<JsonNode> findByName(String name) {
    return getJsonNodes(name, nameIndex);
  }

  public List<JsonNode> findBySchema(String schema) {
    return getJsonNodes(schema, schemaIndex);
  }

  private List<JsonNode> getJsonNodes(String indexedValue, HashMap<String, ArrayList<String>> indexedMap) {
    ArrayList<JsonNode> output = new ArrayList<>();
    try {
      ArrayList<String> idList = indexedMap.get(indexedValue);
      for (String i : idList) {
        output.add(DB2.get(i));
      }
      return output;
    } catch (Exception e) {
      System.out.println("no such schema or name");
      System.out.println(e);
    }
    return output;
  }

//  public void createSchema(String schemaName) {
//    ArrayList<String> schemaArray = new ArrayList<>();
//    schemaIndex.put(schemaName, schemaArray);
//  }

//  public void addToSchema(String schemaName, JsonNode node) {
//    try {
//      storeJson(node);
//      schemaIndex.get(schemaName).add(node.get("id").asText());
//
//    } catch (Exception e) {
//      System.out.println("no such schema were found");
//      System.out.println(e);
//    }
//  }

  public void exportSchema(String schemaName) throws IOException {
    FileUtils.writeLines(
        new File("./" + schemaName + ".txt"), getNodesFromListOfStrings(schemaIndex.get(schemaName)));
  }

  private static List<JsonNode> getNodesFromListOfStrings(ArrayList<String> strings) {
    List<JsonNode> nodes = new ArrayList<>();
    for (String i : strings) {
      nodes.add(findById(i));
    }
    return nodes;
  }

  // import/export database schema
  public void exportDataBaseSchema(String path) throws IOException {
    FileUtils.writeLines(new File(path), retrieveAll());
  }

  public void importDataAndClearExisting(String path) {
    try {
      Scanner scanner = new Scanner(new FileReader(path));

      DB2.clear();
      nameIndex.clear();
      schemaIndex.clear();
      resetId();
      while (scanner.hasNextLine()) {
        String columns = scanner.nextLine();
        JsonNode node = Json.parse(columns);
        DB2.put(node.get("id").asText(), node);
        generatedId = Long.parseLong(node.get("id").asText());
      }
      indexName();
      indexSchema();
      scanner.close();

    } catch (Exception FileNotFoundException) {
      System.out.println("no such file exits");
      System.out.println(FileNotFoundException);
    }
  }

  public List<JsonNode> retrieveAll() {
    return new ArrayList<JsonNode>(DB2.values());
  }

  public static JsonNode findById(String id) {
    return DB2.get(id);
  }

}
