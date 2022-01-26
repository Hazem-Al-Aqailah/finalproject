package com.atypon.finalproject.controllers;

import com.atypon.finalproject.json.JsonDAO;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class WriterUserController {
    JsonDAO dao = JsonDAO.getInstance();

    @GetMapping(value = "welcome-user-writer")
    public String welcomeUserWriter(){
        return "welcome-user-writer";
    }

    @PostMapping(value = "addToDataBaseUser")
    public String addToDataBaseUser(@RequestParam(name = "jsonSource") String jsonSource) {
        System.out.println(jsonSource);
        dao.storeJson(jsonSource);
//        try{
//
//        }catch (JsonProcessingException e){
//            System.out.println(e);
//            System.out.println("bad Json Source");
//        }
        return "redirect:welcome-user-writer";
    }
}
