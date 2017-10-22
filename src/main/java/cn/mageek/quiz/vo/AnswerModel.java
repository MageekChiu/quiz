package cn.mageek.quiz.vo;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


/**
 * 答题表单对象
 * @author Mageek Chiu
 */
public class AnswerModel implements Serializable{

    private String paperId;
    private List<String> answer;

    @Override
    public String toString() {
        return "paperId:"+paperId+
                "answers:"+String.join(",",answer);
    }
}
