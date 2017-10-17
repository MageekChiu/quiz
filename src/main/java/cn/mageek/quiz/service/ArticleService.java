package cn.mageek.quiz.service;


import cn.mageek.quiz.entity.Article;
import cn.mageek.quiz.entity.Person;

import java.util.List;

public interface ArticleService {
    List<Article> findByTitle(String address);
}
