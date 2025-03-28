package com.cliplinker.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.cliplinker.model.ShortenedUrl;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, ShortenedUrl> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, ShortenedUrl> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);

        // Use JSON serialization for objects
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        return template;
    }
}
