package cn.mageek.quiz.service;

import cn.mageek.quiz.entity.User;
import cn.mageek.quiz.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("userService")
public class UserServiceMongoImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceMongoImpl(UserRepository personRepository) {
        this.userRepository = personRepository;
    }


    @Override
    public List<User> getUserListByRole(String role) {
        return userRepository.findByRole(role);
    }

    @Override
    public User findByUsername(String username){
        return userRepository.findByUsername(username);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }


}
