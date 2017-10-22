package cn.mageek.quiz.controller.home;

import cn.mageek.quiz.entity.Paper;
import cn.mageek.quiz.entity.Question;
import cn.mageek.quiz.entity.Tag;
import cn.mageek.quiz.entity.User;
import cn.mageek.quiz.service.QuestionService;
import cn.mageek.quiz.service.TagService;
import cn.mageek.quiz.service.UserService;
import cn.mageek.quiz.vo.InfoModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedList;
import java.util.List;

/**
 * 用户个人
 */
@Controller
@RequestMapping("/me")
public class HomeController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private final QuestionService questionService;
    private final UserService userService;
    private final TagService tagService;
    @Autowired
    public HomeController(QuestionService questionService,UserService userService,TagService tagService) {
        this.questionService = questionService;
        this.userService = userService;
        this.tagService = tagService;
    }

    /**
     * 展示用户信息，比如用户已经做过的试卷选最多3张试卷以及每张卷子当中的最多5道题目
     * 如果没有做过试卷则随机展示15道题目，并引导用户进行三种题目的试卷生成
     * @param model List<Question>
     * @return String view
     */
    @GetMapping(value = {"/","","/info"})
    public String info(Model model){
        //获得Spring Security 里面的用户信息
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUsername(userDetails.getUsername());
        List<Paper> userPapers = user.getPapers(); //用户做过的试卷
        int paperSize = userPapers.size();
        InfoModel infoModel = new InfoModel();
        infoModel.setPaperNum(paperSize);
        List<Question> questionList = new LinkedList<>();
        if(paperSize<1){//没有做过试卷
            infoModel.setHistory(false);
            List<Question> interview = questionService.findRandomNumByTag("java",5);
            List<Question> iq = questionService.findRandomNumByTag("智力测试",5);
            List<Question> turn = questionService.findRandomNumByTag("脑筋急转弯",5);
            //合并
            questionList.addAll(interview);
            questionList.addAll(iq);
            questionList.addAll(turn);
            infoModel.setAvg(0);
            infoModel.setQuestionList(questionList);
        }else{//做过试卷
            infoModel.setHistory(true);
            double avg = 0;
            for (int i =0;i<paperSize;i++){
                Paper paper = userPapers.get(i);
                if (i<3){
                    for (int j= 0;j<paper.getQuestions().size()&& j<5;j++){
                        questionList.add(paper.getQuestions().get(j));
                    }
                }
                avg += paper.getPoint();
            }
            infoModel.setAvg(avg/paperSize);
            infoModel.setQuestionList(questionList);
        }
        model.addAttribute("infoModel",infoModel);
        return "home/home";
    }

    /**
     * 展示某个标签下的问题，要分页
     * @param model
     * @return
     */
    @GetMapping(value = {"/tagquestion/{tag}"})//PathVariable 不能为空
    public String tagQuestion(@PathVariable String tag, Model model){

//        model.addAttribute();
        return "home/index";
    }

    /**
     * 展示笔试题标签，不分页，用户选择过后可以生成试卷
     * @param model
     * @return
     */
    @GetMapping(value = {"/interviewtags"})
    public String interviewtags(Model model){

        List<Tag> tagList = tagService.findAll();
        model.addAttribute("tagList",tagList);
        return "home/tagList";
    }


    /**
     * 生成并展示一套笔试题,开始答题  自动评分
     * @param model
     * @return
     */
    @PostMapping(value = {"/interview"})
    public String interview(@RequestParam("tagList") List<String> tagList, Model model){
        logger.debug("传来的标签：共{}个,为：{}",tagList.size(),String.join(",",tagList));

        model.addAttribute("message","message from controller,hello ");
        return "admin/index";
    }

    /**
     * 展示一套智力测试,开始答题 用户自己评分
     * @param model
     * @return
     */
    @GetMapping(value = {"/iq"})
    public String iq(Model model){
        model.addAttribute("message","message from controller,hello ");
        return "admin/index";
    }

    /**
     * 展示一套脑筋急转弯,开始答题  用户自己评分
     * @param model
     * @return
     */
    @GetMapping(value = {"/turn"})
    public String turn(Model model){
        model.addAttribute("message","message from controller,hello ");
        return "admin/index";
    }

}
