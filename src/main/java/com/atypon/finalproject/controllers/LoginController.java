package com.atypon.finalproject.controllers;

import com.atypon.finalproject.database.DocumentDAO;
import com.atypon.finalproject.database.UserDAO;
import com.atypon.finalproject.users.User;
import com.atypon.finalproject.manger.UserManger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class LoginController {

  UserDAO userDao = UserDAO.getInstance();
  UserManger userManger = UserManger.getManger();

  @GetMapping(value = "/login")
  public String login() {
    return "login";
  }

  @PostMapping(value = "/login")
  public String cred(
      @RequestParam(name = "username") String name,
      @RequestParam(name = "password") String pass,
      Model model) {
    if (userManger.validateUser(name, pass)) {
      User user = userDao.getUser(name);
      if (user.isFirstLogin()) {
        return "redirect:resetpass";
      } else if (user.isAdmin()) {
        return "redirect:welcome-admin";
      } else if (user.canWrite()) {
        return "redirect:welcome-user-writer";
      } else {
        return "redirect:welcome-user-reader";
      }
    }
    model.addAttribute("errorMessage", "Wrong Credentials !!");
    return "login";
  }
}
