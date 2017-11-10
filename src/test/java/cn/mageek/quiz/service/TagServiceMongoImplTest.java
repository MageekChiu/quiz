package cn.mageek.quiz.service;

import cn.mageek.quiz.entity.Paper;
import cn.mageek.quiz.entity.Question;
import cn.mageek.quiz.entity.Tag;
import cn.mageek.quiz.vo.DataPageable;
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
 * @date: 2017-11-09:15:48
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TagServiceMongoImplTest {


    @Autowired
    TagService tagService;

    @Test
    public void findAll() throws Exception {
        List<Tag> tagList = tagService.findAll();
        Assert.assertEquals(12,tagList.size());
    }

    @Test
    public void findInterview() throws Exception {
        List<Tag> tagList = tagService.findInterview();
        Assert.assertNotNull(tagList);
        tagList.forEach(tag -> {
            Assert.assertNotEquals("脑筋急转弯",tag.getName());
            Assert.assertNotEquals("智力测试",tag.getName());
        });
    }

    @Test
    public void getPaperByTags() throws Exception {
        Paper paper = tagService.getPaperByTags(Arrays.asList("java","mysql","计算机网络","算法"));
        Assert.assertNotNull(paper);
        Assert.assertTrue(paper.getQuestions().size()>0);
    }

    @Test
    public void getQuestionListByTag() throws Exception {
        int page = 0 ;
        DataPageable<Question> questionDataPageable = tagService.getQuestionListByTag("java",page,10);
        Assert.assertNotNull(questionDataPageable);
        Assert.assertTrue(questionDataPageable.getContentList().size()>0);
        Assert.assertEquals(questionDataPageable.getCurPage(),page+1);
    }

    @Test
    public void delete() throws Exception {
        Tag tag = tagService.delByID("00000");
        Assert.assertNull(tag);
        tag = tagService.delByID("59f9aeaccbcacd11a87e9972");
        Assert.assertEquals("禁止删除",tag.getName());
    }

    @Test
    public void delByID() throws Exception {
    }

    @Test
    public void save() throws Exception {
    }

    @Test
    public void saveOrReturn() throws Exception {
    }

    @Test
    public void createByTagNameList() throws Exception {
        List<String> nameList = Arrays.asList("java","离散数学");
        List<Tag> tagList = tagService.createByTagNameList(nameList);
        tagList.forEach(tag -> {
            Assert.assertTrue(nameList.contains(tag.getName()));
            Assert.assertTrue(tag.getId().length()>3);
        });
    }

    @Test
    public void updateNameById() throws Exception {
        String newName = "discrete mathematics";
        String id = "5a050af7cbcacd1fc426b7a7";
        Tag tag = tagService.updateNameById(id,newName);
        Assert.assertEquals(newName,tag.getName());
    }

    @Test
    public void findByQuestionID() throws Exception {
        List<Tag> tagList = tagService.findByQuestionID("59f9b3a7cbcacd0704a42731");
        Assert.assertTrue(tagList.size()>0);
        tagList = tagService.findByQuestionID("00000000");
        Assert.assertTrue(tagList.size()==0);
    }

}