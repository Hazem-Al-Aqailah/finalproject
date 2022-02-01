package com.atypon.finalproject.controllers;

import com.atypon.finalproject.communicator.Communicator;
import com.atypon.finalproject.database.DocumentDAO;
import com.atypon.finalproject.manger.UserManger;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URLConnection;

@Controller
@RequestMapping("/")
public class AdminController {

  DocumentDAO dao = DocumentDAO.getInstance();
  UserManger userManger = UserManger.getManger();

  @GetMapping(value = "/welcome-admin")
  public String welcomeAdmin() {
    return "welcome-admin";
  }

  //  @PostMapping(value = "exportSchema")
  //  public String exportSchema(@RequestParam(name = "path") String exportPath) {
  //    System.out.println(exportPath);
  //    dao.exportDataBaseSchema(exportPath);
  //    return "redirect:welcome-admin";
  //  }

  @PostMapping(value = "exportSchema")
  public String exportSchema(HttpServletRequest request, HttpServletResponse response, Model model)
      throws IOException {
    File file = new File("./DataBaseSchema.txt");
    if (file.exists()) {
      // get the mimetype
      String mimeType = URLConnection.guessContentTypeFromName(file.getName());
      if (mimeType == null) {
        // unknown mimetype so set the mimetype to application/octet-stream
        mimeType = "application/octet-stream";
      }
      response.setContentType(mimeType);
      // Here we have mentioned it to show as attachment
      response.setHeader(
          "Content-Disposition", String.format("attachment; filename=\"" + file.getName() + "\""));
      response.setContentLength((int) file.length());

      InputStream inputStream = new BufferedInputStream(new FileInputStream(file));

      FileCopyUtils.copy(inputStream, response.getOutputStream());
      return "redirect:welcome-admin";
    }
    model.addAttribute("errorMessage", "bad data or corrupt file");
    return "welcome-admin";
  }

  @PostMapping(value = "importSchema")
  public String importSchema(@RequestParam(name = "fileUpload") MultipartFile file, Model model) {
    try {
      dao.multipart(file);
      return "redirect:welcome-admin";
    } catch (Exception e) {
      model.addAttribute("errorMessage", "bad data or corrupt file");
      return "welcome-admin";
    }
  }

  @PostMapping(value = "addUser")
  public String addUser(
      @RequestParam(name = "username") String username,
      @RequestParam(name = "password") String pass,
      Model model) {
    if (UserManger.containUser(username)) {
      model.addAttribute("errorMessage", "this user already exists");
      return "welcome-admin";
    }
    userManger.addUser(username, pass);
    return "redirect:welcome-admin";
  }

  @PostMapping(value = "giveUserWrite")
  public String giveUserWrite(@RequestParam(name = "username") String username, Model model) {
    try {
      userManger.giveUserWritePrivilege(username);
    } catch (NullPointerException e) {
      e.printStackTrace();
      model.addAttribute("errorMessage", "no such user exists");
      return "welcome-admin";
    }
    return "redirect:welcome-admin";
  }

  @PostMapping(value = "addToDataBase")
  public String addToDataBase(@RequestParam(name = "jsonSource") String jsonSource) {
    System.out.println(jsonSource);
    dao.storeJson(jsonSource);
    Communicator.updateNodes();
    return "redirect:welcome-admin";
  }

  @PostMapping(value = "deleteFromDataBase")
  public String deleteFromDataBase(@RequestParam(name = "id") String id, Model model) {
    if (dao.containsJson(id)) {
      dao.deleteJson(id);
      Communicator.updateNodes();
      return "redirect:welcome-admin";
    }
    model.addAttribute("errorMessage", "Json for given ID does not exist");
    return "welcome-admin";
  }

  @PostMapping(value = "updateJson")
  public String updateJson(
      @RequestParam(name = "id") String id,
      @RequestParam(name = "jsonSource") String json,
      Model model) {
    if (dao.containsJson(id)) {
      dao.updateJson(id, json);
      Communicator.updateNodes();
      return "redirect:welcome-admin";
    }
    model.addAttribute("errorMessage", "Json for given ID does not exist, or bad Json");
    return "welcome-admin";
  }
}
