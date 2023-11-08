package com.project.lecture.api.complete.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.lecture.redis.UserTierClient;
import com.project.lecture.redis.dto.UserTier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CourseLectureServiceTest {

  @Mock
  private UserTierClient userTierClient;

  @Mock
  private ObjectMapper objectMapper;
  @InjectMocks
  private CourseLectureService courseLectureService;

  @Test
  @DisplayName("완료를 통해서 티어 갱신 로직")
  void completePlusTierByEmailAndTime() throws JsonProcessingException {
    //given
    String email = "planner@gamil.com";
    UserTier userTier = new UserTier(100);
    String userTierStr =  "{\"totalTime\": 50}";

    when(userTierClient.get(anyString()))
        .thenReturn(userTierStr);
    when(objectMapper.readValue(userTierStr,UserTier.class))
        .thenReturn(userTier);
    when(objectMapper.writeValueAsString(any()))
        .thenReturn(userTierStr);
    doNothing().when(userTierClient).put(any(), anyString());

    //when
    courseLectureService.completePlusTierByEmailAndTime(email, 100);

    //then
    verify(userTierClient, timeout(1)).get(anyString());
    verify(objectMapper, timeout(1)).readValue(userTierStr, UserTier.class);
    verify(objectMapper, timeout(1)).writeValueAsString(any());
    verify(userTierClient, timeout(1)).put(any(), anyString());
  }


}