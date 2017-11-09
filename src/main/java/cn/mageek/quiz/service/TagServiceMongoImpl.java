package cn.mageek.quiz.service;


import cn.mageek.quiz.entity.Paper;
import cn.mageek.quiz.entity.Question;
import cn.mageek.quiz.entity.Tag;
import cn.mageek.quiz.repository.QuestionRepository;
import cn.mageek.quiz.repository.TagRepository;
import cn.mageek.quiz.vo.DataPageable;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    public DataPageable<Question> getQuestionListByTag(String tag, int page, int pageSize) {
        DataPageable<Question> questionDataPageable = new DataPageable<>();
//        List<String> questionIdList = mongoTemplate.find();//获得该标签下所有的ID list,不能这样查，只能像下面这样一次拿出一个文档
        Tag tag1 = mongoTemplate.findOne( new Query(Criteria.where("name").is(tag)),Tag.class);
        List<String> questionIdList = tag1.getQuestionIdList();
        questionDataPageable.setAllNUm(questionIdList.size());//总数
        questionDataPageable.setNumPage(pageSize);//每页条数
        questionDataPageable.setAllPage((int) Math.ceil(questionDataPageable.getAllNUm()/pageSize));//总页数
        questionDataPageable.setCurPage(page+1);//当前页
        questionDataPageable.setType(tag);
        //防止超出界限
        int fromINdex = page*pageSize ;
        if (fromINdex>questionDataPageable.getAllNUm()){
            questionDataPageable.setCurPage(-1);//打标记
            questionDataPageable.setContentList(new ArrayList<>());
            questionDataPageable.generatePageList();//生成分页列表避免模板解析错误
            return questionDataPageable;
        }
        int toIndex = questionDataPageable.getAllNUm()<(page+1)*pageSize?questionDataPageable.getAllNUm():(page+1)*pageSize;
        questionIdList = questionIdList.subList(fromINdex,toIndex);//List 分页
        List<Question> questionList = mongoTemplate.find(new Query(Criteria.where("id").in(questionIdList)),Question.class);
        questionDataPageable.setContentList(questionList);
        questionDataPageable.generatePageList();//生成分页列表
        return questionDataPageable;
    }

    @Override
    public void delete(String tagID){
        tagRepository.delete(tagID);
    }

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
        return tagRepository.save(tag);//重复name创建会报duplicate错误，但是对于同一id,亦即主键会采取更新策略，不会检查unique键
//        return mongoTemplate.upsert()
    }

    //如果name存在直接返回，否则创建
    @Override
    public  Tag saveOrReturn(Tag tag){
        Tag oldTag = tagRepository.findDistinctFirstByName(tag.name);
        return  oldTag ==null ? save(tag):oldTag;
    }

    @Override
    public List<Tag> createByTagNameList(List<String> tagNameList){
        List<Tag> tagList = tagNameList.stream().map(tagName -> {
            Tag tag = new Tag();
            tag.setName(tagName);
            tag.setQuestionIdList(new LinkedList<>());
            return saveOrReturn(tag);

        }).collect(Collectors.toList());

        return  tagList;
    }

    @Override
    public Tag updateNameById(String ID, String name){
        return mongoTemplate.findAndModify(Query.query(Criteria.where("id").is(ID)), Update.update("name",name),Tag.class);
    }

    @Override
    public List<Tag> findByQuestionID(String questionID) {
        return tagRepository.findAllByQuestionIdListContains(questionID);
    }


}
