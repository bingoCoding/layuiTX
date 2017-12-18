package com.bingo.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfig {

    private final Logger LOGGER= LoggerFactory.getLogger(RedisConfig.class);
    //主机地址
    @Value("${spring.redis.host}")
    private String host;
    //端口
    @Value("${spring.redis.port}")
    private int port;
    //允许超时
    @Value("${spring.redis.timeout}")
    private int timeout;
    //认证
    @Value("${spring.redis.password}")
    private String password;
    //数据库索引数量
    @Value("${spring.redis.database}")
    private int database;

    @Value("${spring.redis.pool.max-idle}")
    private int maxIdle;

    @Value("${spring.redis.pool.min-idle}")
    private int minIdle;

    @Value("${spring.redis.pool.max-active}")
    private int maxActive;

    @Value("${spring.redis.pool.max-wait}")
    private int maxWait;

    /**
     * @description Jedis数据源配置
     * @return JedisPoolConfig
     */
    @Bean
    JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMinIdle(minIdle);
        jedisPoolConfig.setMaxWaitMillis(maxWait);
        LOGGER.info("Init the RedisPoolConfig Finished");
        return jedisPoolConfig;
    }

    /**
     * @description Jedis数据连接工场
     * @return JedisConnectionFactory
     */
    @Bean
    JedisConnectionFactory redisConnectionFactory(JedisPoolConfig poolConfig) {
        JedisConnectionFactory factory = new JedisConnectionFactory();
        factory.setHostName(host);
        factory.setPort(port);
        factory.setTimeout(timeout);
        factory.setPassword(password);
        factory.setDatabase(database);
        factory.setPoolConfig(poolConfig);
        LOGGER.info("Init the Redis instance Finished");
        return factory;
    }

    @Bean
    RedisTemplate redisTemplate(RedisConnectionFactory factory) {
        StringRedisTemplate template=new StringRedisTemplate(factory);
        setSerializer(template);//序列化，否则long类型等自动转化会报错
        template.afterPropertiesSet();
        return template;
    }


    void setSerializer(StringRedisTemplate template) {
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        template.setValueSerializer(jackson2JsonRedisSerializer);
    }

}
