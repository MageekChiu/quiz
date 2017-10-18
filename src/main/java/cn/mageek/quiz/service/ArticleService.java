package cn.mageek.quiz.service;


import cn.mageek.quiz.entity.Article;

import java.util.List;

public interface ArticleService {
    List<Article> findByTitle(String address);
    Article save(Article article);
}
