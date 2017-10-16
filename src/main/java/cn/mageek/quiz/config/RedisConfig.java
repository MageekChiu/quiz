package cn.mageek.quiz.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import java.util.Arrays;

@Configuration
@Profile("redis")
@EnableRedisHttpSession
public class RedisConfig {


    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory ){
        RedisTemplate<Object,Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);//设置链接工厂

        //自定义 键-值 的序列化 使其可读 刚刚之所以改了好久没有成功是因为 加了@Bean(name="objectRedisTemplate") 导致使用时注入的RedisTemplate redisTemplate;不是这里定义的，而是默认的RedisTemplate
        GenericJackson2JsonRedisSerializer stringSerializer =  new GenericJackson2JsonRedisSerializer();
        template.setValueSerializer(stringSerializer);
        template.setKeySerializer(stringSerializer);
        template.setHashKeySerializer(stringSerializer);
        template.setHashValueSerializer(stringSerializer);

        return template;
    }

    //使用redis作为缓存,需要对象实现Serializable接口
    @Bean
    public CacheManager cacheManager( RedisTemplate redisTemplate ){
        RedisCacheManager redisCacheManager = new RedisCacheManager(redisTemplate);
        redisCacheManager.setCacheNames(Arrays.asList("searches","hello"));
        redisCacheManager.setDefaultExpiration(36_000);
        return redisCacheManager;
    }

    

}
