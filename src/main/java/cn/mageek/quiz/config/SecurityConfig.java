package cn.mageek.quiz.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author Mageek Chiu
 */
//@EnableGlobalMethodSecurity(prePostEnabled = true)//允许进入页面方法前检验
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;

    @Autowired
    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

//    @Bean
//    public BCryptPasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }



    @Autowired
    public void configureAuth(AuthenticationManagerBuilder auth)   throws Exception {

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
        http
                .csrf().
                    disable() //关闭csrf
                .authorizeRequests()
                    .regexMatchers("/admin/.*").authenticated() // 以/admin开头的 要登录
//                    .antMatchers("/webjars/**","**.css","**.js","**.png","**.jpg").permitAll()//不用手动排除，spring boot 已经自动忽略了常见类型静态文件,通过查看源代码 或者打开debug
                    .anyRequest().permitAll()//其余不需要登录
                .and()
                    .formLogin()//自定义登录操作
                    .loginPage("/login")//自定义登录页
                    .defaultSuccessUrl("/admin")//登陆成功后转向
//                    .failureUrl("/login?error")//登录失败转向，其实不用配置，这是默认值
                    .permitAll()//所有人可以登录
                .and()
                    .rememberMe().tokenValiditySeconds(36000).key("quiz")//保存cookie 10小时
                .and()
                    .logout().logoutSuccessUrl("/").permitAll();//所有人可登出，登出后到首页


//               // 强制https 并自定义https端口
//        http
//                .requiresChannel().anyRequest().requiresSecure()
//                .and()
//                .portMapper()
//                .http(8080).mapsTo(8998);

    }
}
