package cn.mageek.quiz.service;


import cn.mageek.quiz.entity.Paper;
import cn.mageek.quiz.entity.User;

import java.security.Principal;
import java.util.List;

public interface PaperService {
    Paper getPaperTransaction(List<String> tagList, Principal principal);
    List<Paper> findByTitle(String address);
    Paper save(Paper paper);
    Paper process(String result, User user);//批改试卷
}
