package com.project.lecture.type;

import lombok.Getter;

@Getter
public enum ResponseType {
  SIGNUP_SUCCESS("회원가입 성공"),COURSE_SUCCESS("강좌등록 성공");

  private final String description;
  ResponseType(String description) {
    this.description = description;
  }
}
