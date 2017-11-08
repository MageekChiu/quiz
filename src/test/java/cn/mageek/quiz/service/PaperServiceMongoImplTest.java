package cn.mageek.quiz.service;

import cn.mageek.quiz.entity.Paper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import sun.security.acl.PrincipalImpl;

import java.security.Principal;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * @Description:
 * @Author: Mageek Chiu
 * @Date: 2017-11-08:21:49
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class PaperServiceMongoImplTest {

    @Autowired
    PaperService paperService;


    @Test
    public void getPaperTransaction() throws Exception {
        Principal principal = new PrincipalImpl("222");
        Paper paper = paperService.getPaperTransaction(Arrays.asList("脑筋急转弯"),principal);
        Assert.assertNotNull(paper);
    }

    @Test
    public void findByTitle() throws Exception {
    }

    @Test
    public void save() throws Exception {
    }

    @Test
    public void process() throws Exception {
    }

}