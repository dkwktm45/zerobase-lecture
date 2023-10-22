package com.project.lecture.exception.kind;

import com.project.lecture.exception.SuperException;

public class NotValidPassword extends SuperException {
  private static final String MESSAGE = "비밀번호가 일치하지 않습니다.";

  public NotValidPassword(){
    super(MESSAGE);
  }

  public NotValidPassword(String name, String message){
    super(MESSAGE);
    addValidation(name,message);
  }

  @Override
  public int getStatusCode(){
    return 400;
  }
}
