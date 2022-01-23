package com.atypon.finalproject.controllers;

import com.atypon.finalproject.json.JsonDAO;
import com.atypon.finalproject.users.Admin;
import com.atypon.finalproject.users.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@RequestMapping("/")
public class AdminController {
  Admin admin = Admin.getAdmin();
  JsonDAO dao = JsonDAO.getInstance();
  @GetMapping(value = "/login")
  public String login() {
    return "login";
  }

  @GetMapping(value = "/resetpass")
  public String resetPass() {
    return "resetpass";
  }

  @GetMapping(value = "/welcome-admin")
  public String welcomeAdmin() {
    return "/welcome-admin";
  }

  @GetMapping(value = "welcomeUser")
  public String welcomeUser(){
    return "redirect:/resetpass";
  }

  @PostMapping(value = "/login")
  public String cred(@ModelAttribute User user, Model model) {
    System.out.println(user.getPassword());
    System.out.println(user.getUsername());
    if (admin.validateAdmin(user.getUsername(), user.getPassword())) {
      if (admin.isFirstLogin()) {
        return "redirect:/resetpass";
      } else {
        return "redirect:/welcome-admin";
      }
    }else if (admin.validateUser(user.getPassword(), user.getUsername())) {
      return "redirect:/welcome-user";
    }
    model.addAttribute("errorMessage", "Wrong Credentials !!");
    return "login";
  }

  @PostMapping(value = "/resetpass")
  public String resetPass(@RequestParam(name = "password") String pass) {
    admin.resetPass(pass);
    return "redirect:/login";
  }

  @PostMapping(value = "exportSchema")
  public String exportSchema(@RequestParam(name = "path") String exportPath) {
    System.out.println(exportPath);
    try {
      dao.exportDataBaseSchema(exportPath);
    }catch(IOException e){
      System.out.println(e);
    }
    return "redirect:/welcome-admin";
  }

  @PostMapping(value = "importSchema")
  public String importSchema(@RequestParam(name = "path") String importPath) {
    System.out.println(importPath);
    dao.importDataAndClearExisting(importPath);
    return "redirect:/welcome-admin";
  }

  @PostMapping(value = "addUser")
  public String addUser(@RequestParam(name = "username") String username, @RequestParam(name = "password") String pass) {
   admin.addUser(username,pass);
   User u = dao.getUser(username);
    System.out.println(u.getPassword());
    System.out.println(u.getUsername());
    return "redirect:/welcome-admin";
  }

  @PostMapping(value = "giveUserWrite")
  public String giveUserWrite(@RequestParam(name = "username") String username) {
    System.out.println(username);
    admin.giveUserWritePrivilege(username);
    return "redirect:/welcome-admin";
  }

  @PostMapping(value = "addToDataBase")
  public String addToDataBase(@RequestParam(name = "jsonSource") String jsonSource) {
    System.out.println(jsonSource);
    try{
      dao.storeJson(jsonSource);
    }catch (JsonProcessingException e){
      System.out.println(e);
      System.out.println("bad Json Source");
    }
    return "redirect:/welcome-admin";
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
