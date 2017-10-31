package cn.mageek.quiz.service;

import cn.mageek.quiz.entity.Question;
import cn.mageek.quiz.entity.Tag;
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

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Administrator
 */
@Service("questionService")
public class QuestionServiceMongoImpl implements QuestionService{

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final QuestionRepository questionRepository;
    private final MongoTemplate mongoTemplate;

    @Autowired
    private TagService tagService;

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

    @Override
    public Question delByID(String id) {
        Question question = mongoTemplate.findAndRemove(Query.query(Criteria.where("id").is(id)),Question.class);
        //TODO 检查对应的标签
        List<Tag> tagList = tagService.findByQuestionID(question.getId());//原先包含这个问题的所有标签
        tagList.stream().map((tag)->tag.getQuestionIdList().remove(id));//删除这个里面的这个问题
        return  question;
    }

    @Override
    public Question findByID(String id) {
        return questionRepository.findOne(id);
    }


    /**
     * 保存或更新question
     * @param question Question
     * @return
     */
    @Override
    public Question save(Question question) {
        Question question1 = questionRepository.save(question);
        //TODO 检查对应的标签
        List<Tag> tagList = tagService.findByQuestionID(question.getId());//原先包含这个问题的所有标签
        tagList.sort(Comparator.comparing(Tag::getName));//按照标签名字排序
        List<String> tagNamesbefore = tagList.stream().map(Tag::getName).collect(Collectors.toList());//原先包含这个问题的所有标签的名字
        List<String> tagNamesNow = question1.getTag();//现在这个问题的所有标签
        tagNamesNow.sort(String::compareTo);//按照名字排序
        for (int i = 0;i<tagNamesbefore.size();i++){
            logger.debug("before:{}",tagNamesbefore.get(i));
            if (!tagNamesNow.contains(tagNamesbefore.get(i))){//删除对应标签里面的id
                Tag tag  = tagList.get(i);
                tag.getQuestionIdList().remove(i);
                tagService.save(tag);
            }
        }

        for (int i=0;i<tagNamesNow.size();i++){
            logger.debug("now:{}",tagNamesNow.get(i));
            String newTagName = tagNamesNow.get(i);
            if (!tagNamesbefore.contains(newTagName)){//添加新增的标签
                List<Tag> tagList1 = tagService.createByTagNameList(Arrays.asList(newTagName));
                Tag tag = tagList1.get(0);
                logger.debug("新增:{},id:{}",newTagName,tag);
                tag.getQuestionIdList().add(question1.getId());
                tagService.save(tag);
            }
        }

        return question1;
    }


}
