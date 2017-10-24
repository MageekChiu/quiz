package cn.mageek.quiz.service;


import cn.mageek.quiz.entity.Paper;
import cn.mageek.quiz.entity.Question;
import cn.mageek.quiz.entity.Tag;
import cn.mageek.quiz.vo.DataPageable;

import java.util.List;

public interface TagService {

    List<Tag> findAll();

    List<Tag> findInterview();

    Paper getPaperByTags(List<String> tags);

    DataPageable<Question> getQuestionListByTag(String tag, int page, int pageSize);

}
