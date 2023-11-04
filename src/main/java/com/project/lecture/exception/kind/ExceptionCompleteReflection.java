package com.project.lecture.exception.kind;

import com.project.lecture.exception.SuperException;

public class ExceptionCompleteReflection extends SuperException {
  private static final String MESSAGE = "이미 완료한 회고입니다.";

  public ExceptionCompleteReflection(){
    super(MESSAGE);
  }

  public ExceptionCompleteReflection(String name, String message){
    super(MESSAGE);
    addValidation(name,message);
  }

  @Override
  public int getStatusCode(){
    return 400;
  }
}
