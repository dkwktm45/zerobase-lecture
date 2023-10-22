package com.project.lecture.type;

import lombok.Getter;

@Getter
public enum ResponseType {
  SIGNUP_SUCCESS("회원가입 성공");

  private final String description;
  ResponseType(String description) {
    this.description = description;
  }
}
