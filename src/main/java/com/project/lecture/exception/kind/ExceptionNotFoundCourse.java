package com.project.lecture.exception.kind;

import com.project.lecture.exception.SuperException;

public class ExceptionNotFoundCourse extends SuperException {
  private static final String MESSAGE = "강좌가 존재하지 않습니다.";

  public ExceptionNotFoundCourse(){
    super(MESSAGE);
  }

  public ExceptionNotFoundCourse(String name, String message){
    super(MESSAGE);
    addValidation(name,message);
  }

  @Override
  public int getStatusCode(){
    return 400;
  }
}
