package cn.mageek.quiz.repository;

import cn.mageek.quiz.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 spring boot mongodb两种方式。
 一种是类似jpa的方式，使用MongoRepository接口，可以自定义方法类似spring-data-jpa。
 一种是使用MongodbOperation类提供的接口。 实现了： 增删改查，分页，批量插入，批量更新，负责条件等。
 * @author Mageek Chiu
 */
public interface QuestionRepository extends MongoRepository<Question,String> {

    List<Question> findByTag(String tag);

    Question findFirstByTag(String tag);

    List<Question> findByType(String type);

    List<Question> findByTagAndAndType(String tag, String type);

}
