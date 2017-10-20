package cn.mageek.quiz.config;

import com.mongodb.Mongo;
import com.mongodb.MongoClientURI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

import java.net.UnknownHostException;

/**
 * @Description: 这个配置是为了直接使用 MongoTemplate
 * @Author: Mageek Chiu
 * @Date: 2017-10-20:22:24
 */
//spring-data-mongodb 配置和使用多个 mongoTemplate - Clement-Xu的专栏 - CSDN博客  http://blog.csdn.net/clementad/article/details/60964802

//@Configuration
public class MongoConfig {

//    @Value("${spring.data.mongodb.uri")
//    private static String MONGO_URI1;
//
//    @Bean
//    public MongoMappingContext mongoMappingContext() {
//        return new MongoMappingContext();
//    }
//
//
//    @Bean //使用自定义的typeMapper去除写入mongodb时的“_class”字段
//    public MappingMongoConverter mappingMongoConverter1() throws Exception {
//        DefaultDbRefResolver dbRefResolver = new DefaultDbRefResolver(this.dbFactory1());
//        MappingMongoConverter converter = new MappingMongoConverter(dbRefResolver, this.mongoMappingContext());
//        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
//        return converter;
//    }
//
//    @Bean
//    @Primary
//    public MongoDbFactory dbFactory1() throws UnknownHostException {
//        return new SimpleMongoDbFactory(new MongoClientURI(MONGO_URI1));
//    }
//
//    @Bean
//    @Primary
//    public MongoTemplate mongoTemplate1() throws Exception {//直接写mongoTemplate 会覆盖默认的 mongoTemplate 导致 security 报错 即使改了也不行，实际上不必定义 spring boot 已经帮我们定义好了
////        return new MongoTemplate(this.dbFactory1(), this.mappingMongoConverter1());
//        return new MongoTemplate(this.dbFactory1());
//    }
}
