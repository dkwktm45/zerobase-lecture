package com.project.lecture.redis;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Slf4j
@Service
public class TokenClient {

  @Value("${jwt.refresh.expiration}")
  private Long TTL;

  private final RedisTemplate<String, String> redisTemplate;

  public String get(String token) {
    return redisTemplate.opsForValue().get(token);
  }

  public void put(String key, String refreshToken) {
    redisTemplate.opsForValue().set(key, refreshToken, TTL, TimeUnit.MICROSECONDS);
  }
}
