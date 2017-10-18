package cn.mageek.quiz.service;

import cn.mageek.quiz.entity.Article;
import cn.mageek.quiz.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("articleService")
public class ArticleServiceImpl implements  ArticleService {

    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleServiceImpl(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Override
    public List<Article> findByTitle(String title) {
        return articleRepository.findByTitle(title);
    }

    @Override
    public Article save(Article article) {
        return articleRepository.save(article);
    }
}
