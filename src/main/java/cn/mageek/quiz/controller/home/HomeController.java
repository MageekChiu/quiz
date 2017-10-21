package cn.mageek.quiz.controller.home;

import cn.mageek.quiz.service.QuestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 用户个人
 */
@Controller
@RequestMapping("/me")
public class HomeController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private final QuestionService questionService;

    @Autowired
    public HomeController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @RequestMapping(value = {"/","","/info"})
    public String hello(@RequestParam(name = "name",defaultValue = "default") String name,
                        Model model){
        model.addAttribute("message","message from controller,hello "+name);
        return "admin/index";
    }


}
