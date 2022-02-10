package com.atypon.finalproject.controllers;

import com.atypon.finalproject.manger.UserManger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class ResetPassController {
  UserManger userManger = UserManger.getManger();

  @GetMapping(value = "/resetpass")
  public String resetPass() {
    return "resetpass";
  }

  @PostMapping(value = "/resetpass")
  public String resetPass(
      @RequestParam(name = "username") String name,
      @RequestParam(name = "oldpassword") String oldPass,
      @RequestParam(name = "newpassword") String newPass,
      Model model) {
    try {
      if (userManger.validateUser(name, oldPass)) {
        userManger.resetPass(name, newPass);
        return "redirect:login";
      }
    } catch (Exception e) {
      model.addAttribute("errorMessage", "Wrong Credentials !!");
    }
    return "resetpass";
  }
}
