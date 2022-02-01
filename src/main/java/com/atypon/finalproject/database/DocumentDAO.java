package com.atypon.finalproject.database;

import com.atypon.finalproject.parser.Json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

@Repository
@Profile("database")
public class DocumentDAO implements BasicDAOInterface {
  @JsonProperty private static long jsonId = 0;

  private static final HashMap<String, JsonNode> DB2 = new HashMap<>();

  private static final DocumentDAO dao = new DocumentDAO();

  @Autowired
  private DocumentDAO() {
    importDataAndClearExisting("DataBaseSchema.txt");
  }

  public static DocumentDAO getInstance() {
    return dao;
  }

  public static synchronized long generateId() {
    return jsonId++;
  }

  private static void resetId() {
    jsonId = 0;
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
      System.out.println("error while exporting ");
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
        jsonId = Long.parseLong(node.get("id").asText());
      }
    } catch (IOException fileNotFoundException) {
      System.out.println("no such file exits");
      fileNotFoundException.printStackTrace();
    }
  }

  public void addToDB(String s) {
    try {
      System.out.println(s);
      JsonNode node = Json.parse(s);
      DB2.put(node.get("id").asText(), node);
      jsonId = Long.parseLong(node.get("id").asText());
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
  }

  public synchronized void multipart(MultipartFile file) {
    try (InputStream inputStream = file.getInputStream(); ) {
      DB2.clear();
      resetId();
      new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
          .lines()
          .forEach(this::addToDB);
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("error in multi part file");
    }
  }

  @Override
  public List<JsonNode> retrieveAll() {
    return new ArrayList<>(DB2.values());
  }

  @Override
  public JsonNode findById(String id) {
    return DB2.get(id);
  }
}
