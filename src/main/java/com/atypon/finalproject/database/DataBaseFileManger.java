package com.atypon.finalproject.database;

import com.atypon.finalproject.utility.Json;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.io.FileUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

import static com.atypon.finalproject.database.DocumentDAO.*;
import static com.atypon.finalproject.database.IDGenerator.*;

public class DataBaseFileManger implements FileManger {

  private DataBaseFileManger(){}

  private static final DataBaseFileManger dataBaseFileManger = new DataBaseFileManger();

  public static DataBaseFileManger getInstance(){
    return dataBaseFileManger;
  }

  @Override
  public synchronized void updateDataBaseFile() {
    try {
      FileUtils.writeLines(new File("./DataBaseSchema.txt"), retrieveAll());
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("error while exporting ");
    }
  }
  // import/export database schema
  @Override
  public synchronized void exportDataBaseSchema(HttpServletResponse response) {
    createDataBaseDownloadFile();
    File file = new File("./DataBaseDownloadFile.txt");
    String mimeType = URLConnection.guessContentTypeFromName(file.getName());
    response.setContentType(mimeType);
    // send the file as an attachment
    response.setHeader(
        "Content-Disposition", String.format("attachment; filename=\"" + file.getName() + "\""));
    response.setContentLength((int) file.length());
    try (InputStream inputStream = new BufferedInputStream(new FileInputStream(file))) {
      FileCopyUtils.copy(inputStream, response.getOutputStream());
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("error when sending the download response");
    }
  }

  private synchronized void createDataBaseDownloadFile() {
    try {
      FileUtils.writeLines(new File("./DataBaseDownloadFile.txt"), retrieveAll());
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("error while exporting ");
    }
  }

  @Override
  public synchronized void importDataAndClearExisting(MultipartFile file) {
    try (InputStream inputStream = file.getInputStream(); ) {
      DB2.clear();
      resetId();
      new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
          .lines()
          .forEach(this::addToDBHashMap);
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("error in multi part file");
    }
  }

  private synchronized void addToDBHashMap(String s) {
    try {
      System.out.println(s);
      JsonNode node = Json.parse(s);
      DB2.put(node.get("id").asText(), node);
      jsonId = Long.parseLong(node.get("id").asText());
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
  }
}
