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
    private final TagService tagService;

    @Autowired
    public QuestionServiceMongoImpl(QuestionRepository questionRepository, MongoTemplate mongoTemplate, TagService tagService) {
        this.questionRepository = questionRepository;
        this.mongoTemplate = mongoTemplate;
        this.tagService = tagService;
    }

    @Override
    public List<Question> findByTagWithStartAndLimit(String tag,Long start,Long limit) {

        return questionRepository.findByTag(tag);
    }

    @Override
    public Question findFirstByTag(String tag) {
        return questionRepository.findFirstByTag(tag);
    }

    /**
     * 根据标签名称随机取出一个问题
     * @param tag 标签名字
     * @return 问题
     */
    @Override
    public Question findRandomOneByTag(String tag){
        //最多取5个出来
        List<Question> questionList = mongoTemplate.find(new Query(Criteria.where("tag").is(tag)).limit(5),Question.class);
        logger.debug("tag:{},个数：{}",tag,questionList.size());
        //随机返回一个
        return questionList.get((int) Math.floor( Math.random()*questionList.size()));
    }

    @Override
    public  List<Question>  findRandomNumByTag(String tag,int num){
        return mongoTemplate.find(new Query(Criteria.where("tag").is(tag)).limit(num),Question.class);
    }

    @Override
    public Question delByID(String id) {
        Question question = mongoTemplate.findAndRemove(Query.query(Criteria.where("id").is(id)),Question.class);
        // 问题不存在 就不用检查标签了
        if(question==null){
            question = new Question();
            question.setId(id);
            return  question;
        }
        // 检查对应的标签
        // 原先包含这个问题的所有标签
        List<Tag> tagList = tagService.findByQuestionID(question.getId());
        // 删除这个标签里面的这个问题
        tagList.forEach((tag)->{
            logger.debug("包含这个问题的tagname:{}",tag.getName());
            tag.getQuestionIdList().remove(id);
            tagService.save(tag);
        });
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
        // 检查对应的标签
        //原先包含这个问题的所有标签
        List<Tag> tagList = tagService.findByQuestionID(question.getId());
        //按照标签名字排序
        tagList.sort(Comparator.comparing(Tag::getName));
        //原先包含这个问题的所有标签的名字
        List<String> tagNamesbefore = tagList.stream().map(Tag::getName).collect(Collectors.toList());
        //现在这个问题的所有标签
        List<String> tagNamesNow = question1.getTag();
        // 按照名字排序
        tagNamesNow.sort(String::compareTo);
        logger.debug("before num : {},after num:{}",tagNamesbefore.size(),tagNamesNow.size());
        // 删除对应标签里面的id
        for (int i = 0;i<tagNamesbefore.size();i++){
            logger.debug("before:{}",tagNamesbefore.get(i));
            if (!tagNamesNow.contains(tagNamesbefore.get(i))){
                Tag tag  = tagList.get(i);
                tag.getQuestionIdList().remove(i);
                tagService.save(tag);
            }
        }
        // 添加新增的标签
        for (int i=0;i<tagNamesNow.size();i++){
            String newTagName = tagNamesNow.get(i);
            logger.debug("now:{}",newTagName);
            if (tagNamesbefore.size()<1 || !tagNamesbefore.contains(newTagName)){
                List<Tag> tagList1 = tagService.createByTagNameList(Arrays.asList(newTagName));
                Tag tag = tagList1.get(0);
                tag.getQuestionIdList().add(question1.getId());
                logger.debug("新增:{},id:{},qid:{}",newTagName,tag,tag.getQuestionIdList().get(0));
                tagService.save(tag);
            }
        }
        return question1;
    }


}
