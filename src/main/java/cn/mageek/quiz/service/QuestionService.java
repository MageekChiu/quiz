package cn.mageek.quiz.service;


import cn.mageek.quiz.entity.Question;

import java.util.List;

public interface QuestionService {

    List<Question> findByTagWithStartAndLimit(String tag, Long start, Long limit);

    Question findFirstByTag(String tag);

    Question findRandomOneByTag(String tag);

    List<Question>  findRandomNumByTag(String tag,int num);

    Question delByID(String id);

    Question findByID(String id);
}
