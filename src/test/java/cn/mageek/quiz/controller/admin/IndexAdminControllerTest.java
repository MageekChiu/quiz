package cn.mageek.quiz.controller.admin;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @Description: 后台首页测试类
 * @Author: Mageek Chiu
 * @Date: 2017-11-08:21:35
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class IndexAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void tagList() throws Exception {
        this.mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void tagDel() throws Exception {
    }

    @Test
    public void tagAdd() throws Exception {
    }

    @Test
    public void tagUpdate() throws Exception {
    }

    @Test
    public void quesList() throws Exception {
    }

    @Test
    public void quesDel() throws Exception {
    }

    @Test
    public void questionShow() throws Exception {
    }

    @Test
    public void quesEdit() throws Exception {
    }

    @Test
    public void userList() throws Exception {
    }

    @Test
    public void paperList() throws Exception {
    }

    @Test
    public void getPersonByAddress() throws Exception {
    }

}