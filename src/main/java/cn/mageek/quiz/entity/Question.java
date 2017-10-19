package cn.mageek.quiz.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.List;

@Document
public class Question implements Serializable{

	@Id //主键
	private Long id;
    private String title;//题目内容
    private String type;//题目类型：选择，简答  两种
    private List<String> tag;//问题的标签：计算机网络，数据结构，Java等等
    private List<String> option;//候选答案列表
    private String answer ;//正确答案

    public Question(Long id, String title, String type, List<String> tag, List<String> option, String answer) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.tag = tag;
        this.option = option;
        this.answer = answer;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getTag() {
        return tag;
    }

    public void setTag(List<String> tag) {
        this.tag = tag;
    }

    public List<String> getOption() {
        return option;
    }

    public void setOption(List<String> option) {
        this.option = option;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
