package com.project.lecture.exception.kind;

import com.project.lecture.exception.SuperException;

public class ExceptionCompletePlanner extends SuperException {
  private static final String MESSAGE = "이미 완료한 항목입니다.";

  public ExceptionCompletePlanner(){
    super(MESSAGE);
  }

  public ExceptionCompletePlanner(String name, String message){
    super(MESSAGE);
    addValidation(name,message);
  }

  @Override
  public int getStatusCode(){
    return 400;
  }
}
