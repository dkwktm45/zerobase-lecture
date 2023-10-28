package com.project.lecture.type;

import lombok.Getter;

@Getter
public enum ResponseType {
  SIGNUP_SUCCESS("가입 성공"),COURSE_SUCCESS("등록 성공"),DELETE_SUCCESS("삭제 성공");

  private final String description;
  ResponseType(String description) {
    this.description = description;
  }
}
