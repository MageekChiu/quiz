package cn.mageek.quiz.controller.admin;

import cn.mageek.quiz.entity.Question;
import cn.mageek.quiz.entity.Tag;
import cn.mageek.quiz.service.PaperService;
import cn.mageek.quiz.service.QuestionService;
import cn.mageek.quiz.service.TagService;
import cn.mageek.quiz.service.UserService;
import cn.mageek.quiz.vo.DataPageable;
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
     * 展示标签列表 也是首页
     * @param model 模型
     * @return 模板
     */
    @RequestMapping(value = {"","/","/tag"})
    public String tagList(Model model){
        List<Tag> tagList = tagService.findAll();
        model.addAttribute("tagList",tagList);
        return "admin/index";
    }


    /**
     * 删除标签，确定该标签没有对应的问题才能删除 否则不能删除
     * @param tagID 标签ID
     * @param redirectAttributes 重定向属性
     * @return 重定向URL
     */
    @RequestMapping(value = {"/tagdel/{tagID}"})
    public String tagDel(@PathVariable String tagID,final RedirectAttributes redirectAttributes){

        Tag tag =  tagService.delByID(tagID);
        redirectAttributes.addFlashAttribute("msg",tag==null?"未找到对应标签":tag.getName());
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
    @PostMapping(value = {"/tagupdate"})
    @ResponseBody
    public String tagUpdate(@RequestBody Tag tag, Model model){
        logger.debug("tag:{}",tag.toString());
        Tag newTag = tagService.updateNameById(tag.getId(),tag.getName());//返回的是修改前的 所以是 oldTag才对
        if(newTag != null) {
            logger.debug("newTag:{}",newTag.toString());
            if(!newTag.getName().equals(tag.getName())){
                return "修改成功";
            }else{
                return "修改失败";
            }
        }else{
            return "所修改的标签不存在";
        }

    }

    /**
     * 展示标签对应的问题列表,可以把某个问题从该标签去掉，和上面一样的步骤
     * @param model
     * @return
     */
    @RequestMapping(value = {"/question"})
    public String quesList(@RequestParam(value = "tagName",defaultValue = "java") String tagName,@RequestParam(value = "page",defaultValue = "1") int page, Model model){
        page = page-1;//前台从1开始，后台从0开始
        DataPageable<Question> questionDataPageable = tagService.getQuestionListByTag(tagName,page,10);
//        logger.debug("questionDataPageable数量:{}",questionDataPageable.getContentList().size());
        model.addAttribute("questionDataPageable",questionDataPageable);
        return "admin/questionList";
    }

    /**
     * 删除问题 检查标签
     * @param model
     * @return
     */
    @RequestMapping(value = {"/questiondel/{quesID}"})
    public String quesDel(@PathVariable String quesID,RedirectAttributes redirectAttributes ,Model model){
        Question question =  questionService.delByID(quesID);
        redirectAttributes.addFlashAttribute("msg",question==null?"删除失败":"删除成功");
        return "redirect:/admin/question";
    }

    /**
     * 展示问题编辑界面
     * @param model
     * @return
     */
    @RequestMapping(value = {"/questionshow/{quesID}"})
    public String questionShow(@PathVariable String quesID ,Model model){
        Question question = questionService.findByID(quesID);
        model.addAttribute("question",question);
        return "admin/editQ";
    }

    /** 修改问题，或者添加问题 检查标签
     * @return
     */
    @RequestMapping(value = {"/questionedit"})
    public String quesEdit(String id,String title,String type,String[] tagNames,String[] options,String answer,Model model){
        logger.debug("question  id:{},title:{}",id,title);

        if("-1".equals(id)){//添加问题
            Question question = new Question();
            question.setTitle(title);
            question.setType(type);
            question.setTag(new LinkedList<>(Arrays.asList(tagNames)));
            question.setOption(new LinkedList<>(Arrays.asList(options)));
            question.setAnswer(answer);
            question = questionService.save(question);
            if (question!=null){
                model.addAttribute("question",question);
                model.addAttribute("msg","添加成功");
            }else{
                model.addAttribute("msg","添加失败");
            }
            return "admin/editQ";
        }else{//修改问题
            Question question = new Question();
            question.setId(id);
            question.setTitle(title);
            question.setType(type);
            question.setTag(new LinkedList<>(Arrays.asList(tagNames)));
            question.setOption(new LinkedList<>(Arrays.asList(options)));
            question.setAnswer(answer);
            question = questionService.save(question);
            if (question!=null){
                model.addAttribute("question",question);
                model.addAttribute("msg","修改成功");
            }else{
                model.addAttribute("msg","修改失败");
            }
        }
        return "admin/editQ";
    }


    /**
     * 展示用户列表
     * @param name
     * @param model
     * @return
     */
    @RequestMapping(value = {"/user"})
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
    @RequestMapping(value = {"/paper"})
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
