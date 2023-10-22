package com.project.lecture.exception.kind;

import com.project.lecture.exception.SuperException;

public class ExistUser extends SuperException {

  private static final String MESSAGE = "이미 존재하는 이메일입니다.";

  public ExistUser(){
    super(MESSAGE);
  }

  public ExistUser(String name, String message){
    super(MESSAGE);
    addValidation(name,message);
  }

  @Override
  public int getStatusCode() {
    return 0;
  }
}
