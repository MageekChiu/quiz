package cn.mageek.quiz.service;

import cn.mageek.quiz.entity.Question;
import cn.mageek.quiz.entity.Tag;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @description:
 * @author: Mageek Chiu
 * @date: 2017-11-10:10:57
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class QuestionServiceMongoImplTest {

    @Autowired
    QuestionService questionService;

    @Autowired
    TagService tagService;


    @Test
    public void findByTagWithStartAndLimit() throws Exception {
    }

    @Test
    public void findFirstByTag() throws Exception {
    }

    @Test
    public void findRandomOneByTag() throws Exception {
        String tagName = "java";
        Question question = questionService.findRandomOneByTag(tagName);
        Assert.assertTrue(question.getTag().contains(tagName));
    }

    @Test
    public void findRandomNumByTag() throws Exception {
    }

    @Test
    public void delByID() throws Exception {
        String id = "5a05442dcbcacd5b70189b9c";
        Question question = questionService.delByID(id);
        // 检查问题本身被删除了
        Assert.assertEquals(id,question.getId());
        List<Tag> tagList = tagService.findByQuestionID(id);
        // 检查标签里面已经没有这个问题了
        Assert.assertTrue(tagList.size()==0);

    }

    @Test
    public void findByID() throws Exception {
    }

    @Test
    public void save() throws Exception {
        Question questionToSave = new Question();questionToSave.setId("5a05442dcbcacd5b70189b9c");questionToSave.setTag(Arrays.asList("java"));questionToSave.setTitle("Java最新版本");questionToSave.setAnswer("9");questionToSave.setType("简答");
        Question questionSaved = questionService.save(questionToSave);
        // 确保已经保存问题本身
        Assert.assertEquals(questionSaved,questionToSave);
        List<Tag> tagList = tagService.findByQuestionID(questionSaved.getId());
        // 确保对应标签正确保存
        Assert.assertTrue(tagList.size()==1);
        Assert.assertEquals(tagList.get(0).getName(),questionSaved.getTag().get(0));

    }

}