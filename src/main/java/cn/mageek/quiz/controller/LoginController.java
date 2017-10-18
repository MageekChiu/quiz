package cn.mageek.quiz.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/login")
@Controller
public class LoginController {

    @GetMapping(value = {"","/"})
    public String loginDisplay(){
        return "login";
    }

//    @PostMapping(value = {"","/"})
//    public String loginProcess(){
//        return "redirect:/admin";
//    }
}
