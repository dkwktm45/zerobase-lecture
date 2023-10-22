package com.project.lecture.exception.kind;

import com.project.lecture.exception.SuperException;

public class NotFoundToken extends SuperException {
  private static final String MESSAGE = "인증되지 않는 사용자입니다.";

  public NotFoundToken(){
    super(MESSAGE);
  }

  public NotFoundToken(String name, String message){
    super(MESSAGE);
    addValidation(name,message);
  }

  @Override
  public int getStatusCode(){
    return 400;
  }
}
