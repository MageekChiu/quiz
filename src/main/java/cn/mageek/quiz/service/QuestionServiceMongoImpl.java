package cn.mageek.quiz.service;

import cn.mageek.quiz.entity.Question;
import cn.mageek.quiz.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.ComparisonOperators;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Administrator
 */
@Service("questionService")
public class QuestionServiceMongoImpl implements QuestionService{

    private final QuestionRepository questionRepository;
    private final MongoTemplate mongoTemplate;

    @Autowired
    public QuestionServiceMongoImpl(QuestionRepository questionRepository,MongoTemplate mongoTemplate) {
        this.questionRepository = questionRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<Question> findByTagWithStartAndLimit(String tag,Long start,Long limit) {

        return questionRepository.findByTag(tag);
    }

    @Override
    public Question findFirstByTag(String tag) {
        return questionRepository.findFirstByTag(tag);
    }

    @Override
    public Question findRandomOneByTag(String tag){
        Question question = new Question(12L,"闰年有几天","简答",Arrays.asList("智力测试"), new ArrayList<>(),"366");
        mongoTemplate.save(question);
        return question;
    }



}
