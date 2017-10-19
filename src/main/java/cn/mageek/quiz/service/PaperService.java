package cn.mageek.quiz.service;


import cn.mageek.quiz.entity.Paper;

import java.util.List;

public interface PaperService {
    List<Paper> findByTitle(String address);
    Paper save(Paper article);
}
