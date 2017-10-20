package cn.mageek.quiz.service;


import cn.mageek.quiz.entity.Question;

import java.util.List;

public interface QuestionService {

    List<Question> findByTagWithStartAndLimit(String tag,Long start,Long limit);

    Question findFirstByTag(String tag);
}
