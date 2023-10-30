package com.project.lecture.type;

import lombok.Getter;

@Getter
public enum ResponseType {
  CHANGE_SUCCESS("수정 성공"),
  SIGNUP_SUCCESS("가입 성공"),
  INSERT_SUCCESS("등록 성공"),
  DELETE_SUCCESS("삭제 성공");

  private final String description;
  ResponseType(String description) {
    this.description = description;
  }
}
