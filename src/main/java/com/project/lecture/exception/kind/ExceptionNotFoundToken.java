package com.project.lecture.exception.kind;

import com.project.lecture.exception.SuperException;

public class ExceptionNotFoundToken extends SuperException {
  private static final String MESSAGE = "인증되지 않는 사용자입니다.";

  public ExceptionNotFoundToken(){
    super(MESSAGE);
  }

  public ExceptionNotFoundToken(String name, String message){
    super(MESSAGE);
    addValidation(name,message);
  }

  @Override
  public int getStatusCode(){
    return 400;
  }
}
