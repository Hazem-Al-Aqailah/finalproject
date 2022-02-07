package com.atypon.finalproject.controllers;

import com.atypon.finalproject.database.DataBaseFileManger;
import com.atypon.finalproject.utility.Communicator;
import com.atypon.finalproject.database.DocumentDAO;
import com.atypon.finalproject.manger.UserManger;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/")
public class AdminController {

  DocumentDAO dao = DocumentDAO.getInstance();
  UserManger userManger = UserManger.getManger();
  DataBaseFileManger dataBaseFileManger = DataBaseFileManger.getInstance();

  @GetMapping(value = "/welcome-admin")
  public String welcomeAdmin() {
    return "welcome-admin";
  }

  @PostMapping(value = "exportSchema")
  public String exportSchema(HttpServletResponse response, Model model) {
    try {
      dataBaseFileManger.exportDataBaseSchema(response);
    } catch (Exception e) {
      model.addAttribute("errorMessage", "Error while preparing the download file");
    }
    return "redirect:welcome-admin";
  }

  @PostMapping(value = "importSchema")
  public String importSchema(@RequestParam(name = "fileUpload") MultipartFile file, Model model) {
    try {
      dataBaseFileManger.importDataAndClearExisting(file);
      Communicator.updateNodes();
    } catch (Exception e) {
      model.addAttribute("errorMessage", "bad data or corrupt file");
    }
    return "redirect:welcome-admin";
  }

  @PostMapping(value = "addUser")
  public String addUser(
      @RequestParam(name = "username") String username,
      @RequestParam(name = "password") String pass,
      Model model) {
    if (!UserManger.containUser(username)) {
      userManger.addUser(username, pass);
    }
    model.addAttribute("errorMessage", "this user already exists");
    return "welcome-admin";
  }

  @PostMapping(value = "giveUserWrite")
  public String giveUserWrite(@RequestParam(name = "username") String username, Model model) {
    try {
      userManger.giveUserWritePrivilege(username);
    } catch (NullPointerException e) {
      model.addAttribute("errorMessage", "no such user exists");
    }
    return "welcome-admin";
  }

  @PostMapping(value = "addToDataBase")
  public String addToDataBase(@RequestParam(name = "jsonSource") String jsonSource, Model model) {
    try {
      dao.storeJson(jsonSource);
      Communicator.updateNodes();
    } catch (Exception e) {
      model.addAttribute("errorMessage", "Bad Json");
    }
    return "welcome-admin";
  }

  @PostMapping(value = "deleteFromDataBase")
  public String deleteFromDataBase(@RequestParam(name = "id") String id, Model model) {
    if (dao.containsJson(id)) {
      dao.deleteJson(id);
      Communicator.updateNodes();
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
    }
    model.addAttribute("errorMessage", "Json for given ID does not exist, or bad Json");
    return "welcome-admin";
  }

  @PostMapping(value = "saveDataBase")
  public String saveDataBase(Model model) {
    try {
      dataBaseFileManger.updateDataBaseFile();
    } catch (Exception e) {
      model.addAttribute("errorMessage", "error while saving the DataBase");
    }
    return "welcome-admin";
  }
}
