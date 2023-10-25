package com.project.lecture.exception.kind;

import com.project.lecture.exception.SuperException;

public class ExceptionNotValidUser extends SuperException {

  private static final String MESSAGE = "일치하지 않는 회원입니다.";

  public ExceptionNotValidUser(){
    super(MESSAGE);
  }

  public ExceptionNotValidUser(String name, String message){
    super(MESSAGE);
    addValidation(name,message);
  }

  @Override
  public int getStatusCode() {
    return 0;
  }
}
