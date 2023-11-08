package com.project.lecture.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserTierClient {

  private final RedisTemplate<String, String> redisTemplate;

  public String get(String key) {
    return redisTemplate.opsForValue().get(key);
  }

  public void put(String key, String UserTierClient) {
    redisTemplate.opsForValue().set(key, UserTierClient);
  }
}
