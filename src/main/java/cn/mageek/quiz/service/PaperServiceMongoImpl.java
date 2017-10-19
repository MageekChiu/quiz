package cn.mageek.quiz.service;

import cn.mageek.quiz.entity.Paper;
import cn.mageek.quiz.repository.PaperRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Administrator
 */
@Service("articleService")
public class PaperServiceMongoImpl implements PaperService {

    private final PaperRepository articleRepository;

    @Autowired
    public PaperServiceMongoImpl(PaperRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Override
    public List<Paper> findByTitle(String title) {
        return articleRepository.findByTitle(title);
    }

    @Override
    public Paper save(Paper article) {
        return articleRepository.save(article);
    }
}
