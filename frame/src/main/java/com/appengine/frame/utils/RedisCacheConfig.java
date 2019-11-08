package com.appengine.frame.utils;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @Author fuyou
 * @Date 2018/12/27 17:57
 */
@EnableCaching
@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class RedisCacheConfig extends CachingConfigurerSupport {

    /**
     * 配置自定义redisTemplate
     *
     * @param cacheRedisFactory
     * @return
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory cacheRedisFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(cacheRedisFactory);
        template.setValueSerializer(jackson2JsonRedisSerializer());
        template.setDefaultSerializer(jackson2JsonRedisSerializer());
        //使用StringRedisSerializer来序列化和反序列化redis的key值
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringRedisSerializer);
        template.setHashKeySerializer(stringRedisSerializer);
        template.setHashValueSerializer(jackson2JsonRedisSerializer());
        template.afterPropertiesSet();
        return template;
    }

    /**
     * json序列化
     *
     * @return
     */
    @Bean
    public RedisSerializer<Object> jackson2JsonRedisSerializer() {
        //使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值
        Jackson2JsonRedisSerializer serializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.enable(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN);
        mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        serializer.setObjectMapper(mapper);
        return serializer;
    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory cacheRedisFactory) {
        // 生成一个默认配置，通过config对象即可对缓存进行自定义配置
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();
        RedisCacheConfiguration config_app_config = RedisCacheConfiguration.defaultCacheConfig();
        RedisCacheConfiguration config_echarts = RedisCacheConfiguration.defaultCacheConfig();
        // 设置缓存的默认过期时间，也是使用Duration设置
        config_app_config = config.entryTtl(Duration.ofMinutes(5))
                // 设置 key为string序列化
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                // 设置value为json序列化
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer()));
        config = config.entryTtl(Duration.ofMinutes(5))
                // 设置 key为string序列化
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                // 设置value为json序列化
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer()))
                // 不缓存空值
                .disableCachingNullValues();
        config_echarts = config.entryTtl(Duration.ofDays(7))
                // 设置 key为string序列化
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                // 设置value为json序列化
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer()))
                // 不缓存空值
                .disableCachingNullValues();

        // 设置一个初始化的缓存空间set集合
        Set<String> cacheNames = new HashSet<>();
        cacheNames.add("app-config");
        cacheNames.add("api-market");
        cacheNames.add("api-echarts");

        // 对每个缓存空间应用不同的配置
        Map<String, RedisCacheConfiguration> configMap = new HashMap<>();
        configMap.put("app-config", config_app_config);
        configMap.put("api-market", config);
        configMap.put("api-echarts", config_echarts);
        RedisCacheManager.RedisCacheManagerBuilder builder = RedisCacheManager.builder(cacheRedisFactory)
                .initialCacheNames(cacheNames)
                .withInitialCacheConfigurations(configMap);
        return builder.build();
    }

}
