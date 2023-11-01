package com.project.lecture.exception.kind;

import com.project.lecture.exception.SuperException;

public class ExceptionCompleteStudy extends SuperException {
  private static final String MESSAGE = "이미 완료한 개인학습입니다.";

  public ExceptionCompleteStudy(){
    super(MESSAGE);
  }

  public ExceptionCompleteStudy(String name, String message){
    super(MESSAGE);
    addValidation(name,message);
  }

  @Override
  public int getStatusCode(){
    return 400;
  }
}
