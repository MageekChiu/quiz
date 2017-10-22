package cn.mageek.quiz.service;


import cn.mageek.quiz.entity.Paper;

import java.util.List;

public interface PaperService {
    List<Paper> findByTitle(String address);
    Paper save(Paper paper);

    Paper process(String result);//批改试卷
}
