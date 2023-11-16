package com.project.lecture.redis;

import com.project.lecture.redis.dto.UserTier;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserTierClient {

  private final RedisTemplate<String, Object> userTierTemplate;


  public UserTier getUserTier(String key) {
    return (UserTier) userTierTemplate.opsForValue().get(key);
  }

  public void putUserTier(String key, UserTier userTier) {
    userTierTemplate.opsForValue().set(key, userTier);
  }
}
