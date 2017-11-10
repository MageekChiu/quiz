package cn.mageek.quiz.service;

import cn.mageek.quiz.entity.User;
import cn.mageek.quiz.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Mageek Chiu
 */
@Service("userDetailsService")
public class UserDeatilsServiceMongoImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public UserDeatilsServiceMongoImpl(UserRepository personRepository) {
        this.userRepository = personRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username);

        logger.debug("username:{}",username);

        if( user == null ){
            throw new UsernameNotFoundException(String.format("User with username=%s was not found", username));
        }else{
            logger.debug("class:{},user:{}",user.getClass(),user.toString());
        }
        return user;
    }

}
