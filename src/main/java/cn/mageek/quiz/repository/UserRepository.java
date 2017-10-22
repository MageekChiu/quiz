package cn.mageek.quiz.repository;

import cn.mageek.quiz.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface UserRepository extends MongoRepository<User, String> {


    User findByUsername(String username);

    List<User> findByRole(String role);

    @Override
    User save(User user);

}
