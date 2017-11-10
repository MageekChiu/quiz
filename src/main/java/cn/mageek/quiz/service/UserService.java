package cn.mageek.quiz.service;


import cn.mageek.quiz.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.jws.soap.SOAPBinding;
import java.util.List;

public interface UserService  {
    List<User> getUserList();

    User findByUsername(String username);

    User save(User user);

    User editKeyVlue(String userID,String key,String value);

    User delete(String userID);

}
