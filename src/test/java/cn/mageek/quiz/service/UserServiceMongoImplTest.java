package cn.mageek.quiz.service;

import cn.mageek.quiz.entity.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import static org.junit.Assert.*;

/**
 * @description:
 * @author: Mageek Chiu
 * @date: 2017-11-10:16:57
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceMongoImplTest {
    @Autowired
    UserService userService;

    @Test
    public void getUserList() throws Exception {

    }

    @Test
    public void findByUsername() throws Exception {
    }

    @Test
    public void save() throws Exception {
    }

    @Test
    public void editKeyVlue() throws Exception {
        String userID = "5a056351cbcacd349cca8068";
        String key = "username";
        String value = "啊哈哈";
        User user = userService.editKeyVlue(userID,key,value);

        // 根据属性名获得描述符
        PropertyDescriptor prop = new PropertyDescriptor(key, User.class);
        // 反射赋值
//        prop.getWriteMethod().invoke(user,value);
        // 反射获值
        String valueSaved = (String) prop.getReadMethod().invoke(user);
        Assert.assertEquals(valueSaved,value);
    }

    @Test
    public void delete() throws Exception {
    }

}