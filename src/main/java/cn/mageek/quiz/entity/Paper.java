package cn.mageek.quiz.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import org.springframework.data.annotation.Id;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Document
public class Paper implements Serializable{
	@Id //主键
    private Long id;
    private String title;
	private List<Question> questions;//问题列表
    private List<String> answers;//回答者的答案
    private int point;//得分
    private int status=-1;//-1 未开始做，0 部分完成，1 完成
    private int seconds;//消耗时间
    private LocalDateTime createTime;//试卷创建时间

    public Paper(Long id, String title, List<Question> questions, List<String> answers, int point, int status, int seconds, LocalDateTime createTime) {
        this.id = id;
        this.title = title;
        this.questions = questions;
        this.answers = answers;
        this.point = point;
        this.status = status;
        this.seconds = seconds;
        this.createTime = createTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }



}
