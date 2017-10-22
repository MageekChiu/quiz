package cn.mageek.quiz.service;

import cn.mageek.quiz.entity.Question;
import cn.mageek.quiz.repository.QuestionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.ComparisonOperators;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Administrator
 */
@Service("questionService")
public class QuestionServiceMongoImpl implements QuestionService{

    private Logger logger = LoggerFactory.getLogger(this.getClass());

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
        // 增
//        Question question = new Question("1","闰年有几天","简答",Arrays.asList("智力测试"), new ArrayList<>(),"366");//主键冲突就更新 不是insert
//        Question question1 = new Question("2","闰年有几天","简答",Arrays.asList("智力测试"), new ArrayList<>(),"366");
//        Question question2 = new Question("3","闰年有几天","简答",Arrays.asList("mysql"), new ArrayList<>(),"366");
//        Question question3 = new Question("4","闰年有几天","简答",Arrays.asList("java"), new ArrayList<>(),"366");
//        Question question5 = new Question("5","闰年有几天","简答",Arrays.asList("脑筋急转弯"), new ArrayList<>(),"366");
//        mongoTemplate.save(question);//Save与insert区别，save是save or update,insert就是insert。;
//        mongoTemplate.save(question1);
//        mongoTemplate.save(question2);
//        mongoTemplate.save(question3);
//        mongoTemplate.save(question5);
        // 改
//        Query query = new Query(Criteria.where("tag").is(tag));
//        Update update = new Update().inc("age", 1);
//        Question p = mongoTemplate.findAndModify(query, update, Question.class); // return's old Question object

        // 查
//        Question question = mongoTemplate.findOne(new Query(Criteria.where("tag").is(tag)),Question.class);
//        return question;

        List<Question> questionList = mongoTemplate.find(new Query(Criteria.where("tag").is(tag)).limit(5),Question.class);//最多取5个出来
        logger.debug("tag:{},个数：{}",tag,questionList.size());
        return questionList.get((int) Math.floor( Math.random()*questionList.size()));//随机返回一个
    }

    @Override
    public  List<Question>  findRandomNumByTag(String tag,int num){
        return mongoTemplate.find(new Query(Criteria.where("tag").is(tag)).limit(5),Question.class);
    }



}
