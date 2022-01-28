package com.atypon.finalproject.controllers;

import com.atypon.finalproject.database.DocumentDAO;
import com.atypon.finalproject.manger.UserManger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
public class AdminController {

  DocumentDAO dao = DocumentDAO.getInstance();
  UserManger userManger = UserManger.getManger();

  @GetMapping(value = "/welcome-admin")
  public String welcomeAdmin() {
    return "welcome-admin";
  }

  @PostMapping(value = "exportSchema")
  public String exportSchema(@RequestParam(name = "path") String exportPath) {
    System.out.println(exportPath);
    dao.exportDataBaseSchema(exportPath);
    return "redirect:welcome-admin";
  }

  @PostMapping(value = "importSchema")
  public String importSchema(@RequestParam(name = "path") String importPath,Model model) {
    try{
      dao.importDataAndClearExisting(importPath);
      return "redirect:welcome-admin";
    }catch (Exception e){
      model.addAttribute("errorMessage","no such file exists");
      return "welcome-admin";
    }

  }

  @PostMapping(value = "addUser")
  public String addUser(@RequestParam(name = "username") String username, @RequestParam(name = "password") String pass,Model model) {
   if (UserManger.containUser(username)){
     model.addAttribute("errorMessage","this user already exists");
     return "welcome-admin";
   }
    userManger.addUser(username,pass);
    return "redirect:welcome-admin";
  }

  @PostMapping(value = "giveUserWrite")
  public String giveUserWrite(@RequestParam(name = "username") String username,Model model) {
    try{
    userManger.giveUserWritePrivilege(username);}
    catch (NullPointerException e){
      e.printStackTrace();
      model.addAttribute("errorMessage","no such user exists");
      return "welcome-admin";
    }
    return "redirect:welcome-admin";
  }

  @PostMapping(value = "addToDataBase")
  public String addToDataBase(@RequestParam(name = "jsonSource") String jsonSource) {
    System.out.println(jsonSource);
    dao.storeJson(jsonSource);
    return "redirect:welcome-admin";
  }

  @PostMapping(value = "deleteFromDataBase")
  public String deleteFromDataBase(@RequestParam(name = "id") String id,Model model ){
    if (dao.containsJson(id)){
      dao.deleteJson(id);
      return "redirect:welcome-admin";
    }
    model.addAttribute("errorMessage","Json for given ID does not exist");
    return "welcome-admin";
  }

  @PostMapping(value = "updateJson")
  public String updateJson(@RequestParam(name = "id") String id,@RequestParam(name = "jsonSource") String json,Model model ){
    if (dao.containsJson(id)){
      dao.updateJson(id,json);
      return "redirect:welcome-admin";
    }
    model.addAttribute("errorMessage","Json for given ID does not exist, or bad Json");
    return "welcome-admin";
  }

//  @PostMapping(value = "createSchema")
//  public String createSchema(@RequestParam(name = "schema") String schema) {
//    System.out.println(schema);
//    dao.createSchema(schema);
//    return "redirect:/welcome-admin";
//  }
//
//  @PostMapping(value = "addToSchema")
//  public String addToSchema(@RequestParam(name = "jsonSource") String jsonSource) {
//    System.out.println(jsonSource);
//    try{
//      dao.storeJson(jsonSource);
//    }catch (JsonProcessingException e){
//      System.out.println(e);
//      System.out.println("bad Json Source");
//    }
//    return "redirect:/welcome-admin";
//  }


}
