package cn.mageek.quiz.controller.admin;

import cn.mageek.quiz.entity.Person;
import cn.mageek.quiz.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author MageekChiu
 */
@Controller
@RequestMapping("/admin")
public class IndexAdminController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public IndexAdminController(PersonService personService) {

    }

    @RequestMapping(value = {"","/"})
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
