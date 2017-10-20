package cn.mageek.quiz.controller.home;

import cn.mageek.quiz.entity.Question;
import cn.mageek.quiz.service.QuestionService;
import cn.mageek.quiz.vo.IndexModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 首页
 */
@Controller
@RequestMapping("/")
public class IndexController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private final QuestionService questionService;
    private IndexModel indexModel;//首页VO

    @Autowired
    public IndexController( QuestionService questionService,IndexModel indexModel) {
        this.questionService = questionService;
        this.indexModel = indexModel;
//        logger.debug("------------{}",indexModel.getInterview().getTitle());//此时还没生成
    }

    @RequestMapping(value={"/","index"})
    public String hello(HttpSession httpSession, Model model){
        //三大类各随机选一道题给前台，保证一次会话的三道题是一样的
        List<Question> questionList = (List<Question>) httpSession.getAttribute("questionList");
//        String.join(",", questionList.get(0).getTag());//list 转字符串
        if (questionList==null){
            logger.debug("-------首次访问先生成并存起来");
            questionList= indexModel.getQuestionList();
            httpSession.setAttribute("questionList",questionList);
        }
        model.addAttribute("questionList",questionList);
        return "home/index";
    }


}
