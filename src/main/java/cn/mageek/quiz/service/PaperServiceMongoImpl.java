package cn.mageek.quiz.service;

import cn.mageek.quiz.entity.Paper;
import cn.mageek.quiz.entity.Question;
import cn.mageek.quiz.entity.User;
import cn.mageek.quiz.repository.PaperRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.Principal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private final UserService userService;
    private final TagService tagService;
    @Autowired
    public PaperServiceMongoImpl(PaperRepository articleRepository,UserService userService,TagService tagService,PaperRepository paperRepository) {
        this.articleRepository = articleRepository;
        this.paperRepository = paperRepository;
        this.userService = userService;
        this.tagService = tagService;
    }

    @Override
    public Paper getPaperTransaction(List<String> tagList, Principal principal) {
        //TODO 检查是否没有question 不然模板会报错
        Paper paper = tagService.getPaperByTags(tagList);//生成试卷题目
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");//修改时间格式
        paper.setTitle(principal.getName()+"--"+dtf.format(paper.getCreateTime()));//修改名字
        Paper paperSaved = save(paper);//存入paper
        User user = userService.findByUsername(principal.getName());//找到用户
        user.getPapers().add(paperSaved);//试卷存入所属用户
        userService.save(user);//保存用户
        return paper;
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
            paper.setSeconds((int) (duration.toMillis()/1000));
            //接下来保存试卷
            Paper paperSaved = paperRepository.save(paper);
            return paperSaved;
        }else{
            return null;
        }

    }
}
