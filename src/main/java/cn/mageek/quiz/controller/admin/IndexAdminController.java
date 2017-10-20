package cn.mageek.quiz.controller.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * @author MageekChiu
 * 后台首页
 */
@Controller
@RequestMapping("/admin")
public class IndexAdminController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @RequestMapping(value = {"","/","/question"})
    public String hello(@RequestParam(name = "name",defaultValue = "default") String name,
                        Model model){
        model.addAttribute("message","message from controller,hello "+name);
        return "admin/index";
    }
    @GetMapping(value="/edit")
    public String getPersonByAddress(){

        return "admin/edit";
    }
}
