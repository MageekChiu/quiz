package cn.mageek.quiz.repository;

import cn.mageek.quiz.entity.Question;
import cn.mageek.quiz.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @author Mageek Chiu
 */
public interface TagRepository extends MongoRepository<Tag,String> {

    @Override
    List<Tag> findAll();


}
