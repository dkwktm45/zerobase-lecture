package com.project.lecture.exception.kind;

import com.project.lecture.exception.SuperException;

public class ExceptionCompleteLecture extends SuperException {
  private static final String MESSAGE = "이미 완료한 강의입니다.";

  public ExceptionCompleteLecture(){
    super(MESSAGE);
  }

  public ExceptionCompleteLecture(String name, String message){
    super(MESSAGE);
    addValidation(name,message);
  }

  @Override
  public int getStatusCode(){
    return 400;
  }
}
