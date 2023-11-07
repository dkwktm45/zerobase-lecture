package com.project.lecture.exception.kind;

import com.project.lecture.exception.SuperException;

public class ExceptionExistListening extends SuperException {
  private static final String MESSAGE = "이미 수강 중이지 않는 강좌입니다.";

  public ExceptionExistListening(){
    super(MESSAGE);
  }

  public ExceptionExistListening(String name, String message){
    super(MESSAGE);
    addValidation(name,message);
  }

  @Override
  public int getStatusCode(){
    return 400;
  }
}
