package cn.mageek.quiz.service;

import cn.mageek.quiz.entity.Tag;
import cn.mageek.quiz.entity.User;
import cn.mageek.quiz.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Mageek Chiu
 */
@Service("userService")
public class UserServiceMongoImpl implements UserService {

    private final UserRepository userRepository;
    private final MongoTemplate mongoTemplate;

    @Autowired
    public UserServiceMongoImpl(UserRepository personRepository,MongoTemplate mongoTemplate) {
        this.userRepository = personRepository;
        this.mongoTemplate = mongoTemplate;
    }


    @Override
    public List<User> getUserList() {
        return userRepository.findAll();
    }

    @Override
    public User findByUsername(String username){
        return userRepository.findByUsername(username);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User editKeyVlue(String userID, String key, String value) {
        return mongoTemplate.findAndModify(Query.query(Criteria.where("id").is(userID)), Update.update(key,value),new FindAndModifyOptions().returnNew(true),User.class);
    }

    @Override
    public User delete(String userID) {
        return mongoTemplate.findAndRemove(Query.query(Criteria.where("id").is(userID)), User.class);
    }


}
