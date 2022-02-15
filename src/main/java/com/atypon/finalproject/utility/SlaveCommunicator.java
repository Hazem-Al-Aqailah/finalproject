package com.atypon.finalproject.utility;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class SlaveCommunicator implements Utility {
  private static final ArrayList<String> slaveNodes = new ArrayList<>();

  private static int slaveNodesPointer = 0;

  private SlaveCommunicator() {}

  public static void addSlaveNode(int port) {
    String url = "http://localhost:" + port + "/api/documents";
    if (slaveNodes.contains(url)) {
      return;
    }
    slaveNodes.add(url);
  }

  public static String getSlaveNode() {
    if (slaveNodesPointer == slaveNodes.size()) {
      slaveNodesPointer = 0;
    }
    try {
      String node = slaveNodes.get(slaveNodesPointer);
      if (healthCheckAndRemoveDeadNode(node)) {
        // returning the pointer to 0 after deletion
        slaveNodesPointer = 0;
        return node + " is down, please refresh the page";
      }
      slaveNodesPointer++;
      return node;
    } catch (IndexOutOfBoundsException e) {
      e.printStackTrace();
      System.out.println("No slave nodes are available");
    }
    return "No slave nodes ATM!";
  }

  public static void updateNodes() {
    for (String nodeUrl : slaveNodes) {
      try {
        sendToNode(nodeUrl, "update");
      } catch (Exception e) {
        e.printStackTrace();
        System.out.println("failed to connect with the node " + nodeUrl);
      }
    }
  }

  private static void sendToNode(String nodeUrl, String message) throws URISyntaxException {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    // adding "/update" to the base uri of the slave node
    URI uri = new URI(nodeUrl + "/update");
    HttpEntity<String> formEntity = new HttpEntity<>(message, headers);
    RestTemplate template = new RestTemplate();
    template.exchange(uri, HttpMethod.POST, formEntity, String.class);
  }

  private static boolean healthCheckAndRemoveDeadNode(String url) {
    try {
      sendToNode(url, "check");
      return false;
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("failed to connect with the node " + url);
      slaveNodes.remove(slaveNodesPointer);
    }
    return true;
  }
}
