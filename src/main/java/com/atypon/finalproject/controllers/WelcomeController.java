package com.atypon.finalproject.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class WelcomeController {

    @GetMapping(value = "/")
    public String redirectToWelcome(){
        return "redirect:welcome";
    }
    @GetMapping(value = "/welcome")
    public String selectAction(){
        return "welcome";
    }
    @PostMapping(value = "read")
    public String redirectRead(){
        return "redirect:welcome-user-reader";
    }
    @PostMapping(value = "write")
    public String redirectWrite(){
        return "redirect:login";
    }
}
