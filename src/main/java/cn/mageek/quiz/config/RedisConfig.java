package cn.mageek.quiz.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import java.util.Arrays;

@Configuration
@Profile("redis")
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 3600)
public class RedisConfig {

    @Bean(name="objectRedisTemplate")
    public RedisTemplate objectRedisTemplate(RedisConnectionFactory redisConnectionFactory ){
        RedisTemplate<Object,Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);//设置链接工厂
        return template;
    }

    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory ){
        RedisTemplate<Object,Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);//设置链接工厂

        // 自定义 键-值 的序列化方法 使其可读
        // 刚刚之所以改了好久没有成功是因为加了@Bean(name="objectRedisTemplate") 导致使用时注入的RedisTemplate redisTemplate 不是这里定义的，而是默认的RedisTemplate
        // 所以这个定义的bean redisTemplate覆盖了原始的bean redisTemplate
//        GenericJackson2JsonRedisSerializer stringSerializer =  new GenericJackson2JsonRedisSerializer();//字符串外边有引号
        StringRedisSerializer stringSerializer =  new StringRedisSerializer();//直接字符串
        template.setValueSerializer(stringSerializer);
        template.setKeySerializer(stringSerializer);
        template.setHashKeySerializer(stringSerializer);
        template.setHashValueSerializer(stringSerializer);

        // 但是这个bean不能作为cacheManager 注入的bean 会报错
        // GenericJackson2JsonRedisSerializer 对应
        // No serializer found for class org.springframework.cache.interceptor.SimpleKey and no properties discovered to create BeanSerializer (to avoid exception, disable SerializationFeature.FAIL_ON_EMPTY_BEANS)
        // StringRedisSerializer 对应
        // java.lang.ClassCastException: org.springframework.cache.interceptor.SimpleKey cannot be cast to java.lang.String
        // 所以这个直接redisTemplate用来客户端操作数据，而objectRedisTemplate用来操作缓存，观察session 貌似也是用的objectRedisTemplate或者说默认的redisTemplate
        return template;
    }

    // 使用redis作为缓存,需要对象实现Serializable接口
    @Bean
    public CacheManager cacheManager(
            @Qualifier("objectRedisTemplate")
                    RedisTemplate redisTemplate ){
        RedisCacheManager redisCacheManager = new RedisCacheManager(redisTemplate);
        redisCacheManager.setCacheNames(Arrays.asList("searches","hello"));
        redisCacheManager.setDefaultExpiration(36_000);
        return redisCacheManager;
    }

}
