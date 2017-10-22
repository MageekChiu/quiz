package cn.mageek.quiz.service;

import cn.mageek.quiz.entity.Paper;
import cn.mageek.quiz.entity.Question;
import cn.mageek.quiz.repository.PaperRepository;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 */
@Service("articleService")
public class PaperServiceMongoImpl implements PaperService {

    private final PaperRepository articleRepository;
    private final PaperRepository paperRepository;

    @Autowired
    public PaperServiceMongoImpl(PaperRepository articleRepository,PaperRepository paperRepository) {
        this.articleRepository = articleRepository;
        this.paperRepository = paperRepository;
    }

    @Override
    public List<Paper> findByTitle(String title) {
        return articleRepository.findByTitle(title);
    }

    @Override
    public Paper save(Paper article) {
        return articleRepository.save(article);
    }

    /**
     * 处理答题结果
     * @param result 答题结果
     * @return
     */
    @Override
    public Paper process(String result) {
        Map<String,String> map = new HashMap<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            // convert JSON string to Map
            map = mapper.readValue(result, new TypeReference<Map<String, String>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(map.containsKey("paperId")){
            String paperId = map.get("paperId");
            Paper paper = paperRepository.findById(paperId);
            if (paper == null){
                return null;
            }
            int point = 0;
            List<Question> questionList = paper.getQuestions();
            List<String> answers = new LinkedList<>();
            for (int i =0 ;i<questionList.size();i++){
                String key = "answer"+i;
                if (map.containsKey(key)){
                    String ans = map.get(key);
                    answers.add(i,ans);
                    if (ans.equals(questionList.get(i).getAnswer())){
                        point +=1;
                    }
                }
            }
            point = 100*point/questionList.size();
            paper.setPoint(point);
            paper.setAnswers(answers);
            paper.setStatus(1);//已完成
            Duration duration = java.time.Duration.between(paper.getCreateTime(), LocalDateTime.now());
            paper.setSeconds((int)Math.ceil(duration.toMinutes()/60));
            //TODO 接下来保存试卷

            return paper;
        }else{
            return null;
        }

    }
}
