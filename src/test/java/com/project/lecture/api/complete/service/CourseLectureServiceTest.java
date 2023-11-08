package com.project.lecture.api.complete.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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


  @InjectMocks
  private CourseLectureService courseLectureService;

  @Test
  @DisplayName("완료를 통해서 티어 갱신 로직")
  void completePlusTierByEmailAndTime() {
    //given
    String email = "planner@gamil.com";
    UserTier userTier = new UserTier(100);

    when(userTierClient.getUserTier(anyString()))
        .thenReturn(userTier);
    doNothing().when(userTierClient).putUserTier(any(), any());

    //when
    courseLectureService.completePlusTierByEmailAndTime(email, 100);

    //then
    verify(userTierClient, timeout(1)).getUserTier(anyString());
    verify(userTierClient, timeout(1)).putUserTier(any(), any());
  }


}