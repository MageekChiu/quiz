package cn.mageek.quiz.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/logina")
@Controller
public class LoginAdminController {

    @GetMapping(value = {"","/"})
    public String loginDisplay(){
        return "admin/login";
    }

}
