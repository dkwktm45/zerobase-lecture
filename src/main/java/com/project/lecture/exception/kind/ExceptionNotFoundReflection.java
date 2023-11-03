package com.project.lecture.exception.kind;

import com.project.lecture.exception.SuperException;

public class ExceptionNotFoundReflection extends SuperException {
  private static final String MESSAGE = "존재하지 않는 회고입니다.";

  public ExceptionNotFoundReflection(){
    super(MESSAGE);
  }

  public ExceptionNotFoundReflection(String name, String message){
    super(MESSAGE);
    addValidation(name,message);
  }

  @Override
  public int getStatusCode(){
    return 400;
  }
}
