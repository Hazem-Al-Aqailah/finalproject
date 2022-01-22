package com.atypon.finalproject.controllers;

import com.atypon.finalproject.json.JsonDAO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/documents")
public class RESTController {
  private final JsonDAO dao = JsonDAO.getInstance();

  public RESTController() {}

  @GetMapping
  public List<JsonNode> all() {
    return dao.retrieveAll();
  }

  @GetMapping("byname/{name}")
  public ResponseEntity<ArrayList> findByName(@PathVariable String name) {
    ArrayList<JsonNode> node = dao.findByName(name);
    if (node == null) {
      return new ResponseEntity(null, HttpStatus.NOT_FOUND);
    }
    return ResponseEntity.ok(node);
  }

  @GetMapping("/{id}")
  public ResponseEntity<JsonNode> findById(@PathVariable String id) {
    JsonNode node = dao.findById(id);
    if (node == null) {
      return new ResponseEntity(null, HttpStatus.NOT_FOUND);
    }
    return ResponseEntity.ok(node);
  }

  @PostMapping("/post")
  public ResponseEntity<String> addToDB(@Validated @RequestBody String jsonSource)
      throws JsonProcessingException {
    if (jsonSource == null) {
      return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }
    dao.storeJson(jsonSource);
    return ResponseEntity.ok(jsonSource);
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<String> removeFromDB(@PathVariable String id) {
    if (dao.containsJson(id)) {
      dao.deleteJson(id);
      return ResponseEntity.ok(id);
    }
    return new ResponseEntity<>(id, HttpStatus.NOT_FOUND);
  }
  @PutMapping("/update/{id}")
  public ResponseEntity<String> updateJson(@PathVariable String id, @Validated @RequestBody String newJson) throws JsonProcessingException {
    if (dao.containsJson(id)) {
      dao.updateJson(id,newJson);
      return ResponseEntity.ok(id);
    }
    return new ResponseEntity<>(id, HttpStatus.NOT_FOUND);
  }

}
