package cn.mageek.quiz.vo;

import cn.mageek.quiz.entity.Question;
import cn.mageek.quiz.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 首页VO
 * @Author: Mageek Chiu
 * @Date: 2017-10-20:19:48
 */
@Component
//@Scope(value= WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)//一次session对应一个
public class IndexModel implements Serializable {//注意是 session scope 而我用的是redis 所以必须序列化，仅仅本类序列化还不够，依赖的类也要序列化 所以question也要序列化才行 不然一直报错
    // Failed to serialize object using DefaultSerializer; nested exception is java.io.NotSerializableException
    // 不仅如此 QuestionServiceMongoImpl也要序列化 这样下去 依赖好多。。。所以不能直接用session scope，还是直接把三个question存session中比较好,或者新建一个只含三道题的正真的VO,
    // 这里我的用法有问题，这样写实际上 IndexModel 成了一个 Service 而不是 VO

    private Question interview;//笔试面试
    private Question iq;//智力测试
    private Question turn;//脑筋急转弯
    private final QuestionService questionService;

    @Autowired
    public IndexModel(QuestionService questionService) {
        this.questionService = questionService;
    }

    public HashMap<String,Question> getQuestionMap(){//依次返回三种类型题目 一类一道
        interview= questionService.findRandomOneByTag("java");
        iq = questionService.findRandomOneByTag("智力测试");
        turn = questionService.findRandomOneByTag("脑筋急转弯");
        HashMap<String,Question> result = new HashMap<>(3);
        result.put("interview",interview);
        result.put("iq",iq);
        result.put("turn",turn);
        return result;
    }

    public Question getInterview() {
        return interview;
    }

    public void setInterview(Question interview) {
        this.interview = interview;
    }

    public Question getIq() {
        return iq;
    }

    public void setIq(Question iq) {
        this.iq = iq;
    }

    public Question getTurn() {
        return turn;
    }

    public void setTurn(Question turn) {
        this.turn = turn;
    }


}
