package com.atypon.finalproject.controllers;

import com.atypon.finalproject.database.DocumentDAO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class WriterUserController {
  DocumentDAO dao = DocumentDAO.getInstance();

  @GetMapping(value = "welcome-user-writer")
  public String welcomeUserWriter() {
    return "welcome-user-writer";
  }

  @PostMapping(value = "addToDataBaseUser")
  public String addToDataBaseUser(
      @RequestParam(name = "jsonSource") String jsonSource, Model model) {
    try {
      dao.storeJson(jsonSource);
    } catch (Exception e) {
      model.addAttribute("errorMessage", "Bad Json");
    }
    return "welcome-user-writer";
  }
}
