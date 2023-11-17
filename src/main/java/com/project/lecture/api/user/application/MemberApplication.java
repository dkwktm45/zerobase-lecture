package com.project.lecture.api.user.application;

import com.project.lecture.redis.UserTierClient;
import com.project.lecture.redis.dto.UserTier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberApplication {

  private final UserTierClient userTierClient;

  public UserTier getUserTier(String email) {
    return userTierClient.getUserTier(email);
  }
}
