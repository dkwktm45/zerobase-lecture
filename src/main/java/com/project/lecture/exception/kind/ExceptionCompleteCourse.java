package com.project.lecture.exception.kind;

import com.project.lecture.exception.SuperException;

public class ExceptionCompleteCourse extends SuperException {
  private static final String MESSAGE = "이미 완료한 강좌입니다.";

  public ExceptionCompleteCourse(){
    super(MESSAGE);
  }

  public ExceptionCompleteCourse(String name, String message){
    super(MESSAGE);
    addValidation(name,message);
  }

  @Override
  public int getStatusCode(){
    return 400;
  }
}
