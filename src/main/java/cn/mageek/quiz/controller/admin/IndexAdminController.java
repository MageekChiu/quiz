package cn.mageek.quiz.controller.admin;

import cn.mageek.quiz.entity.Tag;
import cn.mageek.quiz.service.PaperService;
import cn.mageek.quiz.service.QuestionService;
import cn.mageek.quiz.service.TagService;
import cn.mageek.quiz.service.UserService;
import org.apache.tomcat.util.buf.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.LinkedList;
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
        List<Tag> tagList = tagService.findAll();
        model.addAttribute("tagList",tagList);
        return "admin/index";
    }


    /**
     * 删除标签，但是删除问题的时候要检查该问题已经没有其他标签了 否则不能删除
     * @param tagID
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = {"/tagdel/{tagID}"})
    public String tagDel(@PathVariable String tagID,final RedirectAttributes redirectAttributes){


        Tag tag =  tagService.delByID(tagID);

//        logger.debug("tagID:{},tag:{}",tagID,tag!=null?tag.toString():"null");

        redirectAttributes.addFlashAttribute("msg",tag==null?"删除失败":"删除成功");
        return "redirect:/admin";

    }

    /**
     * 增加标签 但是不增加问题 即空数组
     * @param tagNames
     * @return
     */
    @PostMapping(value = {"/tagadd"})
    public String tagAdd(String[] tagNames, Model model){

        LinkedList tagNameList = new LinkedList<String>(Arrays.asList(tagNames));//转成list

        logger.debug("tagNameList:{}", StringUtils.join(tagNameList,','));

        List<Tag> tagList = tagService.createByTagNameList(tagNameList);
        model.addAttribute("msg","创建成功如下,数量："+tagList.size());
        model.addAttribute("tagList",tagList);
        return "admin/index";
    }

    /**
     * 修改标签,可以修改名字
     * @param model
     * @return
     */
    @RequestMapping(value = {"/tagupdate"})
    public String tagUpdate(Model model){
        List<Tag> tagList = tagService.findInterview();
        model.addAttribute("tagList",tagList);
        return "admin/index";
    }

    /**
     * 展示标签对应的问题列表,可以把某个问题从该标签去掉，和上面一样的步骤
     * @param model
     * @return
     */
    @RequestMapping(value = {"/question/{tagID}/{page}"})
    public String quesList(@PathVariable String tagID,@PathVariable int page, Model model){
        model.addAttribute("message","");
        return "admin/index";
    }

    /**
     * 删除问题 检查标签
     * @param model
     * @return
     */
    @RequestMapping(value = {"/questiondel/{quesID}"})
    public String quesDel(@PathVariable String quesID, Model model){
        model.addAttribute("message","");
        return "admin/index";
    }

    /**
     * 添加问题，检查标签，若无则新增标签
     * @param model
     * @return
     */
    @RequestMapping(value = {"/questionadd"})
    public String quesAdd(@PathVariable String quesID, Model model){
        model.addAttribute("message","");
        return "admin/index";
    }

    /**
     * 展示用户列表
     * @param name
     * @param model
     * @return
     */
    @RequestMapping(value = {"/userlist"})
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
    @RequestMapping(value = {"/paperlist"})
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
