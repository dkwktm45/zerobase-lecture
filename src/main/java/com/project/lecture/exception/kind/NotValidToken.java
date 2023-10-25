package com.project.lecture.exception.kind;

import com.project.lecture.exception.SuperException;

public class NotValidToken extends SuperException {
  private static final String MESSAGE = "유효하지 않는 토큰입니다.";

  public NotValidToken(){
    super(MESSAGE);
  }

  public NotValidToken(String name){
    super(MESSAGE);
    addValidation(name,MESSAGE);
  }

  @Override
  public int getStatusCode(){
    return 400;
  }
}
