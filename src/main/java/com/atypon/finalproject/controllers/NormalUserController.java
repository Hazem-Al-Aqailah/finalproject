package com.atypon.finalproject.controllers;

import com.atypon.finalproject.utility.SlaveCommunicator;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class NormalUserController {

  @GetMapping(value = "welcome-user-reader")
  public String welcomeUserReader(Model model) {
    model.addAttribute("link", SlaveCommunicator.getSlaveNode());
    return "welcome-user-reader";
  }
}
