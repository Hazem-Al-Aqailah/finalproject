package com.atypon.finalproject.controllers;

import com.atypon.finalproject.utility.SlaveCommunicator;
import com.atypon.finalproject.database.DocumentDAO;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/documents")
public class RESTController {
  private final DocumentDAO dao = DocumentDAO.getInstance();

  @GetMapping
  public List<JsonNode> all() {
    return dao.retrieveAll();
  }

  // this mapping is only used to connect with the slave node upon their lunch
  @PostMapping(value = "/receiver")
  public void addNode(HttpServletRequest request) {
    int port = request.getRemotePort() + 1;
    SlaveCommunicator.addSlaveNode(port);
    System.out.println("node with port " + port + " is connected");
  }
}
