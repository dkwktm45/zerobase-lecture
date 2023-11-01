package com.project.lecture.exception.kind;

import com.project.lecture.exception.SuperException;

public class ExceptionNotFoundStudy extends SuperException {
  private static final String MESSAGE = "존재하지 않는 개인학습입니다.";

  public ExceptionNotFoundStudy(){
    super(MESSAGE);
  }

  public ExceptionNotFoundStudy(String name, String message){
    super(MESSAGE);
    addValidation(name,message);
  }

  @Override
  public int getStatusCode(){
    return 400;
  }
}
