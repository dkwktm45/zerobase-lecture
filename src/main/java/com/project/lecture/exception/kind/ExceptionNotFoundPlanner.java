package com.project.lecture.exception.kind;

import com.project.lecture.exception.SuperException;

public class ExceptionNotFoundPlanner extends SuperException {
  private static final String MESSAGE = "존재하지 않는 플랜입니다.";

  public ExceptionNotFoundPlanner(){
    super(MESSAGE);
  }

  public ExceptionNotFoundPlanner(String name, String message){
    super(MESSAGE);
    addValidation(name,message);
  }

  @Override
  public int getStatusCode(){
    return 400;
  }
}
