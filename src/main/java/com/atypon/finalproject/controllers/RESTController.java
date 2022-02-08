package com.atypon.finalproject.controllers;

import com.atypon.finalproject.utility.Communicator;
import com.atypon.finalproject.database.DocumentDAO;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/documents")
public class RESTController {
  private final DocumentDAO dao = DocumentDAO.getInstance();

  public RESTController() {}

  @GetMapping
  public List<JsonNode> all() {

    return dao.retrieveAll();
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
  public ResponseEntity<String> addToDB(@Validated @RequestBody String jsonSource) {
    if (jsonSource == null) {
      return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }
    try {
      dao.storeJson(jsonSource);
      return ResponseEntity.ok(jsonSource);
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("error during parsing the json ");
    }
    return new ResponseEntity<>(jsonSource, HttpStatus.BAD_REQUEST);
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
  public ResponseEntity<String> updateJson(
      @PathVariable String id, @Validated @RequestBody String newJson) {
    if (dao.containsJson(id)) {
      dao.updateJson(id, newJson);
      return ResponseEntity.ok(id);
    }
    return new ResponseEntity<>(id, HttpStatus.NOT_FOUND);
  }

  // this mapping is only used to connect with the slave node upon their lunch
  @PostMapping(value = "/receiver")
  public void addNode(HttpServletRequest request) {
    int port = request.getRemotePort() + 1;
    Communicator.addSlaveNode(port);
    System.out.println("node with port " + port + " is connected");
  }
}
