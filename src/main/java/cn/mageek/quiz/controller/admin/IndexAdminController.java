package cn.mageek.quiz.controller.admin;

import cn.mageek.quiz.entity.Tag;
import cn.mageek.quiz.service.PaperService;
import cn.mageek.quiz.service.QuestionService;
import cn.mageek.quiz.service.TagService;
import cn.mageek.quiz.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author MageekChiu
 * 后台首页
 */
@Controller
@RequestMapping("/admin")
public class IndexAdminController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final QuestionService questionService;
    private final UserService userService;
    private final TagService tagService;
    private final PaperService paperService;

    @Autowired
    public IndexAdminController(QuestionService questionService,UserService userService,TagService tagService,PaperService paperService) {
        this.questionService = questionService;
        this.userService = userService;
        this.tagService = tagService;
        this.paperService = paperService;
    }
    /**
     * 展示标签列表
     * @param name
     * @param model
     * @return
     */
    @RequestMapping(value = {"","/","/tag"})
    public String tagList(@RequestParam(name = "name",defaultValue = "脑筋急转弯") String name,Model model){
        List<Tag> tagList = tagService.findInterview();
        model.addAttribute("tagList",tagList);
        return "admin/index";
    }

    /**
     * 展示标签对应的问题列表
     * @param name
     * @param model
     * @return
     */
    @RequestMapping(value = {"/question"})
    public String quesList(@RequestParam(name = "tag",defaultValue = "脑筋急转弯") String name,
                          Model model){
        model.addAttribute("message","message from controller,hello "+name);
        return "admin/index";
    }

    /**
     * 展示用户列表
     * @param name
     * @param model
     * @return
     */
    @RequestMapping(value = {"","/","/question"})
    public String userList(@RequestParam(name = "tag",defaultValue = "脑筋急转弯") String name,
                           Model model){
        model.addAttribute("message","message from controller,hello "+name);
        return "admin/index";
    }

    /**
     * 展示用户对应的试卷列表
     * @param name
     * @param model
     * @return
     */
    @RequestMapping(value = {"","/","/question"})
    public String paperList(@RequestParam(name = "tag",defaultValue = "脑筋急转弯") String name,
                           Model model){
        model.addAttribute("message","message from controller,hello "+name);
        return "admin/index";
    }

    @GetMapping(value="/edit")
    public String getPersonByAddress(){

        return "admin/edit";
    }
}
