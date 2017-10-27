package cn.mageek.quiz.entity;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import org.springframework.data.annotation.Id;
import java.io.Serializable;
import java.util.List;

/**
 * @Description:
 * @Author: Mageek Chiu
 * @Date: 2017-10-20:19:14
 */
@Document
public class Tag implements Serializable {
    @Id
    public String id;
    @Indexed(unique = true)
    public String name; //标签名,唯一
//    @DBRef //不建议使用
//    public List<Question> questions;//使用引用 因为问题可能很多，直接存会可能超过16M限制，引用（虽然也可能超过限制）可以增大存储量

    public List<String> questionIdList;//手动引用是建议的

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getQuestionIdList() {
        return questionIdList;
    }

    public void setQuestionIdList(List<String> questionIdList) {
        this.questionIdList = questionIdList;
    }
//
//    public List<Question> getQuestions() {
//        return questions;
//    }
//
//    public void setQuestions(List<Question> questions) {
//        this.questions = questions;
//    }


    @Override
    public String toString() {
        return "id:"+id+
                "，name:"+name+
                "，size:"+questionIdList.size();
    }
}
