package cn.mageek.quiz.controller.home;

import cn.mageek.quiz.entity.Article;
import cn.mageek.quiz.entity.User;
import cn.mageek.quiz.service.ArticleService;
import cn.mageek.quiz.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/")
public class IndexController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private final UserService userService;
    private final RedisTemplate redisTemplate;
    private final ArticleService articleService;

    @Autowired
    public IndexController(UserService userService, RedisTemplate redisTemplate, ArticleService articleService) {
        this.userService = userService;
        this.redisTemplate = redisTemplate;
        this.articleService = articleService;
    }

    @RequestMapping("/")
    public String hello(@RequestParam(name = "name",defaultValue = "default") String name,
                        Model model){
        model.addAttribute("message","message from controller,hello "+name);
        return "home/index";
    }

    @RequestMapping(value="/person/{address}")
    @ResponseBody
    public List<User> getPersonByAddress(@PathVariable String address){
        List<User> personList = null;
        try {
            personList = userService.getUserListByRole(address);
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return  personList;
    }

    @RequestMapping(value="redis")
    @ResponseBody
    public String getPersonByAddress(HttpSession httpSession){

        httpSession.setAttribute("expire",1800000);//手工设置session
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        HashOperations<String,String,String> valueOperations1 = redisTemplate.opsForHash();
        valueOperations.set("add","asdasdsadsad");
        valueOperations1.put("sds","sadsdas","sdaas");
        valueOperations1.put("sds","saas","是不是");
        return valueOperations.get("add");
    }

    @Cacheable("searches")
    @RequestMapping(value="cache")
    @ResponseBody
    public User getCache(){
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new User();
    }

    @RequestMapping(value="mongo")
    @ResponseBody
    public List<User> getArticle(@RequestParam(value = "role",defaultValue = "root") String role){
//        userService.save(new User(22L,"111","111","root"));
//        userService.save(new User(1L,"222","222","admin"));
//        userService.save(new User(3L,"333","333","root"));
        List<User> userList = userService.getUserListByRole(role);
//        userList.add(userService.getUserListByRole("admin").get(0));
        return userList;
    }


}
