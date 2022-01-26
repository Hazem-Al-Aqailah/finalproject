package com.atypon.finalproject.controllers;

import com.atypon.finalproject.users.UserManger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class ResetPassController {

    @GetMapping(value = "/resetpass")
    public String resetPass() {
        return "resetpass";
    }
    @PostMapping(value = "/resetpass")
    public String resetPass(@RequestParam(name = "username") String name, @RequestParam(name = "oldpassword") String oldPass, @RequestParam(name = "newpassword") String newPass, Model model) {
        try{
        if (UserManger.validateUser(name, oldPass)) {
        UserManger.resetPass(name,newPass);
        return "redirect:login";
        }}catch (NullPointerException e){
            e.printStackTrace();
        }
        model.addAttribute("errorMessage", "Wrong Credentials !!");
        return "resetpass";
    }
}
