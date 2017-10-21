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

/**
 * @author Mageek Chiu
 * Example of Multiple Login Pages With Spring Security and Spring Boot - DZone Security  https://dzone.com/articles/example-of-multiple-login-pages-with-spring-securi
 * 这个不管用
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
    @Order(1)//必须加order 不然 bean冲突
    public  class AdminSecurityConfig extends WebSecurityConfigurerAdapter {

        //    @Bean
//    public BCryptPasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

        @Autowired
        public void configureAuth(AuthenticationManagerBuilder auth) throws Exception {

//        // 内存中建立账号密码，这个会使得 application.properties的密码配置不管用
//        auth.inMemoryAuthentication()
//                .withUser("111").password("111").roles("USER").and()
//                .withUser("admin").password("admin").roles("USER", "ADMIN");

            auth.userDetailsService(userDetailsService);

//        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
//        // 指定密码加密所使用的加密器为passwordEncoder()需要将密码加密后写入数据库

        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            System.out.println("bbbbbbbbbbbbbbbbbbbbbbnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn");
            http
                    .csrf().disable() //关闭csrf
                    .authorizeRequests()
                    .antMatchers("/admin**").authenticated() // 以/admin开头的 要登录
//                    .antMatchers("/me**").authenticated()
//                    .antMatchers("/webjars/**","**.css","**.js","**.png","**.jpg").permitAll()//不用手动排除，spring boot 已经自动忽略了常见类型静态文件,通过查看源代码 或者打开debug
//                    .anyRequest().permitAll()//其余不需要登录
                    .and()
                    .formLogin()//自定义登录操作
                    .loginPage("/logina")//自定义登录页
                    .loginProcessingUrl("/logina")//貌似这两个保持一致才可以
                    .defaultSuccessUrl("/admin")//登陆成功后转向
                    .failureUrl("/logina?error")//登录失败转向，其实不用配置，这是默认值
                    .permitAll()//所有人可以登录
                    .and()
                    .rememberMe().tokenValiditySeconds(36000).key("quiz")//保存cookie 10小时
                    .and()
                    .logout().logoutUrl("/logouta")
                    .logoutSuccessUrl("/").permitAll();//所有人可登出，登出后到首页

            //这样写也不管用 要分成两个类来写
//                .and()
//                    .authorizeRequests()
//                    .regexMatchers("/me.*")
//                    .authenticated()
//                    .anyRequest().permitAll()
//                .and()
//                    .formLogin()//自定义登录操作
//                    .loginPage("/login")//自定义登录页
//                    .loginProcessingUrl("/login")//貌似这两个保持一致才可以
//                    .defaultSuccessUrl("/")//登陆成功后转向
//                    .permitAll();//所有人可以登录


//               // 强制https 并自定义https端口
//        http
//                .requiresChannel().anyRequest().requiresSecure()
//                .and()
//                .portMapper()
//                .http(8080).mapsTo(8998);

//        http.authorizeRequests().regexMatchers().authenticated();//这样写不管用
        }
    }

    /**
     * 前台安全类
     */
    @Configuration
    @Order(2)
    public  class HomeSecurityConfig extends WebSecurityConfigurerAdapter {

        @Autowired
        public void configureAuth(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(userDetailsService);
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            System.out.println("aaaaaaaaaaaaaaaaaaaaajjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjj");//打印了，表示执行了但是为啥不管用
            http
                    .csrf().disable() //关闭csrf
                    .authorizeRequests()
                    .antMatchers("/me**").authenticated() // 以/me开头的 要登录
                    .anyRequest().permitAll()//其余不需要登录
                    .and()
                    .formLogin()//自定义登录操作
                    .loginPage("/login")//自定义登录页
                    .loginProcessingUrl("/login")//貌似这两个保持一致才可以
                    .defaultSuccessUrl("/")//登陆成功后转向
                    .failureUrl("/login?error")//登录失败转向，其实不用配置，这是默认值
                    .permitAll()//所有人可以登录
                    .and()
                    .rememberMe().tokenValiditySeconds(36000).key("quiz")//保存cookie 10小时
                    .and()
                    .logout().logoutUrl("/logout").logoutSuccessUrl("/").permitAll();//所有人可登出，登出后到首页

        }

    }


}
