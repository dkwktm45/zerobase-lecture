package com.project.lecture.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.lecture.exception.kind.NotFoundUser;
import com.project.lecture.redis.dto.RefreshToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;


@RequiredArgsConstructor
@Slf4j
@Service
public class RedisClient {

  @Value("${jwt.refresh.expiration}")
  private Long TTL;

  private final RedisTemplate<String, Object> redisTemplate;
  private static final ObjectMapper mapper = new ObjectMapper();

  public <T> T get(String token, Class<T> classType) {
    return getObject(token, classType);
  }

  private <T> T getObject(String key, Class<T> classType) {
    String redisValue = (String) redisTemplate.opsForValue().get(key);
    if (ObjectUtils.isEmpty(redisValue)) {
      return null;
    }else {
      try{
        return mapper.readValue(redisValue, classType);
      } catch (JsonProcessingException e) {
        log.error("parsing error",e);
        return null;
      }
    }
  }
  public void put(String accessToken, RefreshToken refreshToken) {
    putObject(accessToken,refreshToken);
  }
  private void putObject(String key, RefreshToken refreshToken) {
    try {
      redisTemplate.opsForValue().set(key, mapper.writeValueAsString(refreshToken), TTL);
    } catch (JsonProcessingException e) {
      throw new NotFoundUser();
    } catch (RedisSystemException e) {
      e.getMessage();
    }
  }

  public void delete(String token) {
    deleteObject(token);
  }
  private void deleteObject(String key) {
    redisTemplate.delete(key);
  }
}
