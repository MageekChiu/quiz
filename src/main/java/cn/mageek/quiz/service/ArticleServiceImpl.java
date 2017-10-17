package cn.mageek.quiz.service;

import cn.mageek.quiz.entity.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("articleService")
public class ArticleServiceImpl implements  ArticleService {

    private final ArticleService articleService;

    @Autowired
    public ArticleServiceImpl(ArticleService articleService) {
        this.articleService = articleService;
    }

    @Override
    public List<Article> findByTitle(String address) {
        return articleService.findByTitle(address);
    }
}
