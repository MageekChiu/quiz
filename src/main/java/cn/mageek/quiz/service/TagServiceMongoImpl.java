package cn.mageek.quiz.service;


import cn.mageek.quiz.entity.Paper;
import cn.mageek.quiz.entity.Question;
import cn.mageek.quiz.entity.Tag;
import cn.mageek.quiz.repository.QuestionRepository;
import cn.mageek.quiz.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.data.mongodb.core.query.Query;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagServiceMongoImpl implements TagService {

    private final TagRepository tagRepository;
    private final QuestionRepository questionRepository;
    private final MongoTemplate mongoTemplate;

    @Autowired
    public TagServiceMongoImpl(TagRepository tagRepository,QuestionRepository questionRepository,MongoTemplate mongoTemplate) {
        this.tagRepository = tagRepository;
        this.questionRepository = questionRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<Tag> findAll(){
        return tagRepository.findAll();
    }

    @Override
    public List<Tag> findInterview() {
        return mongoTemplate.find(new Query(Criteria.where("name").nin(Arrays.asList("脑筋急转弯","智力测试"))),Tag.class);
    }

    @Override
    public Paper getPaperByTags(List<String> tags){
        Paper paper = new Paper();
        if (tags.size()>20){//最多只取20个标签
            tags = tags.subList(0,20);
        }

        List<Question> questionAllList = new LinkedList<>();
        List<Question> questionList;

        for (String tag:tags){
            questionList = questionRepository.findByTag(tag);//该标签所有的
            questionAllList.addAll(questionList.subList(0, Math.min(questionList.size(), 20)));//该标签取最多20个
        }

        //去重
        List<Question> questionUniqList = questionAllList.stream().distinct().collect(Collectors.toList());
        //打乱
        Collections.shuffle(questionUniqList);
        //最多取20道题
        paper.setQuestions(questionUniqList.subList(0, Math.min(questionUniqList.size(), 20)));
        paper.setCreateTime(LocalDateTime.now());
        return paper;
    }

}
