package com.project.lecture.exception.kind;

import com.project.lecture.exception.SuperException;

public class ExceptionExistUser extends SuperException {

  private static final String MESSAGE = "이미 존재하는 이메일입니다.";

  public ExceptionExistUser(){
    super(MESSAGE);
  }

  public ExceptionExistUser(String name){
    super(MESSAGE);
    addValidation(name,MESSAGE);
  }

  @Override
  public int getStatusCode() {
    return 500;
  }
}
