package cn.mageek.quiz.service;


import cn.mageek.quiz.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.jws.soap.SOAPBinding;
import java.util.List;

public interface UserService  {
    List<User> getUserListByRole(String role);

    User save(User user);
}
