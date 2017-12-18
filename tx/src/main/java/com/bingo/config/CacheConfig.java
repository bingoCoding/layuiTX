package com.bingo.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.Any;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.shiro.cache.CacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import java.lang.reflect.Method;
import java.sql.Statement;

import static com.github.pagehelper.util.MetaObjectUtil.method;

/**
 * redis缓存管理配置
 */
@EnableCaching
@Configuration
public class CacheConfig extends CachingConfigurerSupport {

    private final Logger LOGGER = LoggerFactory.getLogger(CacheConfig.class);

    //允许超时
    @Value("${spring.redis.timeout}")
    private int timeout;

    @Bean
    RedisCacheManager cacheManager(RedisTemplate redisTemplate){
        RedisCacheManager redisCacheManager = new RedisCacheManager(redisTemplate);
        //设置key-value过期时间
        redisCacheManager.setDefaultExpiration(timeout);
        LOGGER.info("初始化Redis缓存管理器完成!");
        return redisCacheManager;
    }

    /**
     * @description 缓存保存策略 自定义key生成器
     * @return KeyGenerator
     *
     * 定义缓存数据 key 生成策略的bean
     * 包名+类名+方法名+所有参数
     *
     */
    @Bean
    public KeyGenerator wiselyKeyGenerator() {
        return new KeyGenerator() {
            @Override
            public Object generate(Object target, Method method, Object... params) {
                StringBuilder sb = new StringBuilder();
                sb.append(target.getClass().getName());
                sb.append(method.getName());
                for (Object obj : params) {
                    sb.append(obj.toString());
                }
                return sb.toString();
            }
        };
    }


}

