package cn.mageek.quiz.controller.home;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/login")
@Controller
public class LoginController {

    @GetMapping(value = {"","/"})
    public String loginDisplay(){
        return "home/login";
    }

}
