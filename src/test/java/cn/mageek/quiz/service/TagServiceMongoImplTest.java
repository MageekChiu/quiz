package cn.mageek.quiz.service;

import cn.mageek.quiz.entity.Paper;
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
    }

    @Test
    public void delete() throws Exception {
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
    }

}