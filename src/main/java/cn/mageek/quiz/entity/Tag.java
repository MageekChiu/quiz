package cn.mageek.quiz.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
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
    public Long id;
    public String name; //标签名
    public List<Long> questionId;//使用这个标签的问题的id

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Long> getQuestionId() {
        return questionId;
    }

    public void setQuestionId(List<Long> questionId) {
        this.questionId = questionId;
    }


}
