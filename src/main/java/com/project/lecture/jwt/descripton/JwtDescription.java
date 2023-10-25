package com.project.lecture.jwt.descripton;

import lombok.Getter;

@Getter
public enum JwtDescription {
  ACCESS_TOKEN_SUBJECT("AccessToken"),
  REFRESH_TOKEN_SUBJECT("RefreshToken"),
  EMAIL_CLAIM("email"),
  BEARER("Bearer ");

  private final String value;
  JwtDescription(String value) {
    this.value = value;
  }
}
