package com.project.lecture.exception.kind;

import com.project.lecture.exception.SuperException;

public class ExceptionNotFoundUser extends SuperException {
  private static final String MESSAGE = "이메일 존재하지 않습니다.";

  public ExceptionNotFoundUser(){
    super(MESSAGE);
  }

  public ExceptionNotFoundUser(String name, String message){
    super(MESSAGE);
    addValidation(name,message);
  }

  @Override
  public int getStatusCode(){
    return 400;
  }
}
