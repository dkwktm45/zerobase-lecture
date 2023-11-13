package com.project.lecture.redis;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class LectureClient {

  private final HashOperations<String, Integer, Object> opsHashLectureTime;

  public LectureClient(RedisTemplate<String, Object> lectureTimeTemplate) {
    this.opsHashLectureTime = lectureTimeTemplate.opsForHash();
  }

  public Integer getLectureTime(Long courseId, Long lectureId) {
    return (Integer) opsHashLectureTime.get(String.valueOf(courseId), lectureId);
  }

  public List<Integer> getKeys(Long courseId){
    return new ArrayList<>(opsHashLectureTime.keys(String.valueOf(courseId)));
  }

  public void putLecture(Long courseId, Long lectureId, Integer lectureTime) {
    String courseKey = String.valueOf(courseId);
    opsHashLectureTime.put(courseKey, Math.toIntExact(lectureId), lectureTime);
  }
}
