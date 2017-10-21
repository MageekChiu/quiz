package cn.mageek.quiz.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.servlet.Filter;

/**
 * @author Mageek Chiu
 * Example of Multiple Login Pages With Spring Security and Spring Boot - DZone Security  https://dzone.com/articles/example-of-multiple-login-pages-with-spring-securi
 * 这个不管用 而且先进入me 就可以不登录进入admin
 * java - Spring Security 3.2.1 Multiple login forms with distinct WebSecurityConfigurerAdapters - Stack Overflow  https://stackoverflow.com/questions/22845474/spring-security-3-2-1-multiple-login-forms-with-distinct-websecurityconfigurerad
 *
 */
//@EnableGlobalMethodSecurity(prePostEnabled = true)//允许进入页面方法前检验
@Configuration
public class SecurityConfig {

    private  UserDetailsService userDetailsService;

    @Autowired
    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
    /**
     * 后台安全类
     */
    @Configuration
    public class AdminSecurityConfig extends WebSecurityConfigurerAdapter {

        @Autowired
        public void configureAuth(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(userDetailsService);
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            System.out.println("bbbbbbbbbbbbbbbbbbbbbbnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn");
            http
                    .csrf().disable() //关闭csrf
                    .authorizeRequests()
                    .regexMatchers("/admin.*").hasAnyRole("ROOT","ADMIN")  // 以/admin开头的 要管理员权限
                    .regexMatchers("/me.*").hasAnyRole("ROOT","ADMIN","USER")  // 以/me 开头的 要管理员或者用户权限 总之要登录
                    .anyRequest().permitAll()//其余不需要登录
                    .and()
                        .formLogin()//自定义登录操作
                        .loginPage("/logina")//自定义登录页
                        .loginProcessingUrl("/logina")//貌似这两个保持一致才可以
                        .defaultSuccessUrl("/admin")//登陆成功后转向
                        .failureUrl("/logina?error")//登录失败转向，
                    .and()
                        .rememberMe()
                        .tokenValiditySeconds(36000)
                        .key("quiz")//保存cookie 10小时
                    .and()
                        .logout()
                        .logoutUrl("/logouta")
                        .logoutSuccessUrl("/").permitAll();//所有人可登出，登出后到首页
        }
    }

}