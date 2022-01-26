package com.atypon.finalproject.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class NormalUserController {

    @GetMapping(value = "welcome-user-reader")
    public String welcomeUserReader(){
        return "welcome-user-reader";
    }
}
