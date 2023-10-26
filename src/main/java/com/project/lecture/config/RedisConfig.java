package com.project.lecture.config;

import com.project.lecture.redis.dto.RefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableRedisRepositories
@RequiredArgsConstructor
public class RedisConfig {

  @Bean
  public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
    RedisTemplate<String,String> redisTemplate = new RedisTemplate<>();
    StringRedisSerializer serializer = new StringRedisSerializer();

    redisTemplate.setConnectionFactory(redisConnectionFactory);
    redisTemplate.setKeySerializer(serializer);
    redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(RefreshToken.class));
    redisTemplate.setHashKeySerializer(serializer);
    redisTemplate.setHashValueSerializer(serializer);
    return redisTemplate;
  }
  @Bean
  public RedisMessageListenerContainer redisMessageListener(RedisConnectionFactory redisConnectionFactory) {
    RedisMessageListenerContainer container = new RedisMessageListenerContainer();
    container.setConnectionFactory(redisConnectionFactory);
    return container;
  }

}
