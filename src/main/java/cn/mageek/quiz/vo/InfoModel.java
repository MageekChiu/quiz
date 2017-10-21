package cn.mageek.quiz.vo;

import cn.mageek.quiz.entity.Question;
import cn.mageek.quiz.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * @Description: 用户信息VO
 * @Author: Mageek Chiu
 * @Date: 2017-10-20:19:48
 */
public class InfoModel implements Serializable {
    private List<Question> questionList;
    private boolean history;//是否做过题
    private int paperNum;//做过的试卷数量
    private double avg;//平均分分

    public List<Question> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(List<Question> questionList) {
        this.questionList = questionList;
    }

    public boolean isHistory() {
        return history;
    }

    public void setHistory(boolean history) {
        this.history = history;
    }

    public int getPaperNum() {
        return paperNum;
    }

    public void setPaperNum(int paperNum) {
        this.paperNum = paperNum;
    }

    public double getAvg() {
        return avg;
    }

    public void setAvg(double avg) {
        this.avg = avg;
    }
}
