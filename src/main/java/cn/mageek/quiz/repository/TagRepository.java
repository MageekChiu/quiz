package cn.mageek.quiz.repository;

import cn.mageek.quiz.entity.Question;
import cn.mageek.quiz.entity.Tag;
import com.mongodb.client.model.FindOneAndDeleteOptions;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @author Mageek Chiu
 */
public interface TagRepository extends MongoRepository<Tag,String> {


    /**
     * 不需要 override 直接用就行了 但是这个删除无法确定是否删除成功
     * @param tag
     */
    @Override
    void delete(Tag tag);

    List<Tag> findAllByQuestionIdListContains(String questionID);

    Tag findDistinctFirstByName(String name);

}
