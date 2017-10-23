package cn.mageek.quiz.controller.home;

import cn.mageek.quiz.entity.Paper;
import cn.mageek.quiz.entity.Question;
import cn.mageek.quiz.entity.Tag;
import cn.mageek.quiz.entity.User;
import cn.mageek.quiz.service.PaperService;
import cn.mageek.quiz.service.QuestionService;
import cn.mageek.quiz.service.TagService;
import cn.mageek.quiz.service.UserService;
import cn.mageek.quiz.vo.AnswerModel;
import cn.mageek.quiz.vo.InfoModel;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.Principal;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

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
//    private final UserDetails userDetails;
    private final PaperService paperService;
    @Autowired
    public HomeController(QuestionService questionService,UserService userService,TagService tagService,PaperService paperService) {
        this.questionService = questionService;
        this.userService = userService;
        this.tagService = tagService;
        //获得Spring Security 里面的用户信息 不能放构造函数里 因为此时还没有
//        this.userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        this.paperService = paperService;
    }

    /**
     * 展示用户信息，比如用户已经做过的试卷选最多3张试卷以及每张卷子当中的最多5道题目
     * 如果没有做过试卷则随机展示15道题目，并引导用户进行三种题目的试卷生成
     * @param model List<Question>
     * @return String view
     */
    @GetMapping(value = {"/","","/info"})
    public String info(Model model, Principal principal){
//        User user = userService.findByUsername(userDetails.getUsername());
        User user = userService.findByUsername(principal.getName());
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
            //注意要去重
            infoModel.setQuestionList(questionList.stream().distinct().collect(Collectors.toList()));
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
        List<Tag> tagList = tagService.findInterview();
        model.addAttribute("tagList",tagList);
        return "home/tagList";
    }

    /**
     * 展示一套智力测试,开始答题 整套题答完后 用户自己评分
     * @param model
     * @return
     */
    @GetMapping(value = {"/iq"})
    public String iq(HttpSession httpSession, Model model, Principal principal){
        Paper paper = getPaper(httpSession, Arrays.asList("智力测试"), principal);
        model.addAttribute("paper",paper);
        return "home/paper";
    }

    /**
     * 展示一套脑筋急转弯,开始答题  每一道题答完用户可以查看答案，自己评分
     * @param model
     * @return
     */
    @GetMapping(value = {"/turn"})
    public String turn(HttpSession httpSession,Model model,Principal principal){
        Paper paper = getPaper(httpSession, Arrays.asList("脑筋急转弯"), principal);
        model.addAttribute("paper",paper);
        return "home/paper";
    }

    /**
     * 生成并展示一套笔试题,开始答题  自动评分
     * @param model
     * @return
     */
    @PostMapping(value = {"/interview"})
    public String interview(HttpSession httpSession,@RequestParam("tagList") List<String> tagList, Model model, Principal principal){
        logger.debug("传来的标签，共{}个,为：{}",tagList.size(),String.join(",",tagList));
        Paper paper = getPaper(httpSession, tagList, principal);
        model.addAttribute("paper",paper);
        return "home/paper";
    }

    /**
     * 生成试卷 同时检查重复提交
     * @param httpSession
     * @param tagList
     * @param principal
     * @return
     */
    private Paper getPaper(HttpSession httpSession, @RequestParam("tagList") List<String> tagList, Principal principal) {
        //防止重复生成试卷的操作
        String paperRecentString = (String) httpSession.getAttribute("paperRecent");
        Paper paper = null;
        if (paperRecentString!=null){
            paper = JSON.parseObject(paperRecentString,new TypeReference<Paper>(){});
        }
        if (paper==null){
            paper = paperService.getPaperTransaction(tagList, principal);
            String paperString = JSON.toJSONString(paper);
            httpSession.setAttribute("paperRecent",paperString);
            logger.debug("生成了paper:{}",paperString);
        }else{
            paper.setStatus(0);
            logger.debug("使用了已有paper:{}",paper.toString());
        }
        return paper;
    }


    /**
     * 提交答题结果 批卷并保存
     * @param result
     * @param model
     * @param principal
     * @return
     */
    @PostMapping(value = {"/getresult"})
    public String getResult(@RequestParam("result") String result, HttpSession httpSession,Model model, Principal principal){
        // 这里也要防止重复提交，交给服务层去做
        httpSession.removeAttribute("paperRecent");//提交一个试卷后才允许再创建试卷
        logger.debug(result);
        Paper paperSaved = paperService.process(result);
        User user = userService.findByUsername(principal.getName());
        user.getPapers().add(paperSaved);
        userService.save(user);//保存用户
        model.addAttribute("paper",paperSaved);
        return "home/paper";
    }

}
