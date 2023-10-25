package com.project.lecture.redis.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RefreshToken {
  private String email;

  private String refreshToken;

  private String accessToken;
}