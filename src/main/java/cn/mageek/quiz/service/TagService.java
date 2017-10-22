package cn.mageek.quiz.service;


import cn.mageek.quiz.entity.Paper;
import cn.mageek.quiz.entity.Question;
import cn.mageek.quiz.entity.Tag;

import java.util.List;

public interface TagService {

    List<Tag> findAll();

    Paper getPaperByTags(List<String> tags);

}
