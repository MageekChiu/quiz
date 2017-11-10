package cn.mageek.quiz.service;


import cn.mageek.quiz.entity.Paper;
import cn.mageek.quiz.entity.Question;
import cn.mageek.quiz.entity.Tag;
import cn.mageek.quiz.repository.QuestionRepository;
import cn.mageek.quiz.repository.TagRepository;
import cn.mageek.quiz.vo.DataPageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.data.mongodb.core.query.Query;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Mageek Chiu
 */
@Service
public class TagServiceMongoImpl implements TagService {

    private final TagRepository tagRepository;
    private final QuestionRepository questionRepository;
    private final MongoTemplate mongoTemplate;

    private static int TAG_MAX_NUM = 20;

    @Autowired
    public TagServiceMongoImpl(TagRepository tagRepository,QuestionRepository questionRepository,MongoTemplate mongoTemplate) {
        this.tagRepository = tagRepository;
        this.questionRepository = questionRepository;
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * 查找所有标签
     * @return tagList
     */
    @Override
    public List<Tag> findAll(){
        return tagRepository.findAll();
    }

    /**
     * 查找笔试面试题的标签，亦即除去"脑筋急转弯","智力测试"
     * @return tagList
     */
    @Override
    public List<Tag> findInterview() {
        return mongoTemplate.find(new Query(Criteria.where("name").nin(Arrays.asList("脑筋急转弯","智力测试"))),Tag.class);
    }

    /**
     * 根据标签列表得出一套试卷,这里不确定试卷名称
     * @param tags
     * @return
     */
    @Override
    public Paper getPaperByTags(List<String> tags){
        //最多只取 TAG_MAX_NUM 个标签
        if (tags.size()>TAG_MAX_NUM){
            tags = tags.subList(0,TAG_MAX_NUM);
        }
        List<Question> questionAllList = new LinkedList<>();
        List<Question> questionList;
        for (String tag:tags){
            //该标签所有的
            questionList = questionRepository.findByTag(tag);
            //该标签取最多20个
            questionAllList.addAll(questionList.subList(0, Math.min(questionList.size(), TAG_MAX_NUM)));
        }
        //去重
        List<Question> questionUniqList = questionAllList.stream().distinct().collect(Collectors.toList());
        //打乱
        Collections.shuffle(questionUniqList);
        //最后，最多取20道题
        questionUniqList =  questionUniqList.subList(0, Math.min(questionUniqList.size(), 20));
        //防止为空,避免controller检查
        if (questionUniqList.size()<1){
            questionUniqList.add(new Question());
        }
        Paper paper = new Paper();
        paper.setQuestions(questionUniqList);
        paper.setCreateTime(LocalDateTime.now());
        return paper;
    }

    /**
     * 获得特定标签的问题列表
     * @param tag 标签名字
     * @param page 页数 从0 开始 但是页面上是从1开始的，所以返回要加1
     * @param pageSize 页面列表数量
     * @return
     */
    @Override
    public DataPageable<Question> getQuestionListByTag(String tag, int page, int pageSize) {
        DataPageable<Question> questionDataPageable = new DataPageable<>();
        Tag tag1 = mongoTemplate.findOne( new Query(Criteria.where("name").is(tag)),Tag.class);
        List<String> questionIdList = tag1.getQuestionIdList();
        questionDataPageable.setAllNUm(questionIdList.size());
        questionDataPageable.setNumPage(pageSize);
        questionDataPageable.setAllPage((int) Math.ceil(questionDataPageable.getAllNUm()/pageSize));
        questionDataPageable.setCurPage(page+1);
        questionDataPageable.setType(tag);
        //防止超出界限
        int fromINdex = page*pageSize ;
        if (fromINdex>questionDataPageable.getAllNUm()){
            //打标记
            questionDataPageable.setCurPage(-1);
            questionDataPageable.setContentList(new ArrayList<>());
            questionDataPageable.generatePageList();//生成分页列表避免模板解析错误
            return questionDataPageable;
        }
        int toIndex = questionDataPageable.getAllNUm()<(page+1)*pageSize?questionDataPageable.getAllNUm():(page+1)*pageSize;
        //获得本页的id列表
        questionIdList = questionIdList.subList(fromINdex,toIndex);
        List<Question> questionList = mongoTemplate.find(new Query(Criteria.where("id").in(questionIdList)),Question.class);
        questionDataPageable.setContentList(questionList);
        questionDataPageable.generatePageList();
        return questionDataPageable;
    }

    /**
     * 直接删除 没有反馈
     * @param tagID 标签ID
     */
    @Override
    public void delete(String tagID){
        tagRepository.delete(tagID);
    }

    /**
     * 检查删除的合理性
     * @param tagID 标签ID
     * @return 标签 或者 null
     */
    @Override
    public Tag delByID(String tagID){
        Tag tag = mongoTemplate.findOne(Query.query(Criteria.where("id").is(tagID)), Tag.class);
        //没有对应标签
        if (tag == null){
            return null;
        }
        // 还有问题存在 禁止删除标签
        if(tag.getQuestionIdList().size()>0){
            tag.setName("禁止删除");
        }else{
            mongoTemplate.remove(Query.query(Criteria.where("id").is(tagID)), Tag.class);
            tag.setName("删除成功");
        }
        return tag;
    }

    @Override
    public Tag save(Tag tag) {
        // 重复name创建会报duplicate错误，但是对于同一id,亦即主键会采取更新策略，不会检查unique键
        return tagRepository.save(tag);
    }

    // 如果name存在直接返回，否则创建
    @Override
    public  Tag saveOrReturn(Tag tag){
        Tag oldTag = tagRepository.findDistinctFirstByName(tag.name);
        return  oldTag ==null ? save(tag):oldTag;
    }

    /**
     * 根据名字创建一系列标签
     * @param tagNameList
     * @return
     */
    @Override
    public List<Tag> createByTagNameList(List<String> tagNameList){

        return tagNameList.stream().map(tagName -> {
            Tag tag = new Tag();
            tag.setName(tagName);
            tag.setQuestionIdList(new LinkedList<>());
            return saveOrReturn(tag);

        }).collect(Collectors.toList());
    }

    @Override
    public Tag updateNameById(String id, String newName){
        // 返回旧的
//        return mongoTemplate.findAndModify(Query.query(Criteria.where("id").is(id)), Update.update("name",newName),Tag.class);
        // 返回新的
        return mongoTemplate.findAndModify(Query.query(Criteria.where("id").is(id)), Update.update("name",newName),new FindAndModifyOptions().returnNew(true),Tag.class);
    }

    /**
     * 找到某个问题对应的所有标签
     * @param questionID 问题ID
     * @return 标签列表
     */
    @Override
    public List<Tag> findByQuestionID(String questionID) {
        return tagRepository.findAllByQuestionIdListContains(questionID);
    }


}
