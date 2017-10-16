package cn.mageek.quiz.controller.admin;

import cn.mageek.quiz.entity.Person;
import cn.mageek.quiz.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class IndexController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private final PersonService personService;

    @Autowired
    public IndexController(PersonService personService) {
        this.personService = personService;
    }

    @RequestMapping("/")
    public String hello(@RequestParam(name = "name",defaultValue = "default") String name,
                        Model model){
        model.addAttribute("message","message from controller,hello "+name);
        return "home/index";
    }
    @RequestMapping(value="/person/{address}")
    @ResponseBody
    public List<Person> getPersonByAddress(@PathVariable String address){
        List<Person> personList = null;
        try {
            personList = personService.findByAddress(address);
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return  personList;
    }
}
