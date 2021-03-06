package cn.mageek.quiz.repository;


import cn.mageek.quiz.entity.Paper;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 spring boot mongodb两种方式。
 一种是类似jpa的方式，使用MongoRepository接口，可以自定义方法类似spring-data-jpa。
 一种是使用MongodbOperation类提供的接口。 实现了： 增删改查，分页，批量插入，批量更新，负责条件等。
 * @author Administrator
 */
public interface PaperRepository extends MongoRepository<Paper,String> { //类和主键类型要一致

    List<Paper> findByTitle(String title);

    Paper findById(String id);

}
