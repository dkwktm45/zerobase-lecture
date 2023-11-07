package com.project.lecture.exception.kind;

import com.project.lecture.exception.SuperException;

public class ExceptionNotFoundLecture extends SuperException {
  private static final String MESSAGE = "강의가 존재하지 않습니다.";

  public ExceptionNotFoundLecture(){
    super(MESSAGE);
  }

  public ExceptionNotFoundLecture(String name, String message){
    super(MESSAGE);
    addValidation(name,message);
  }

  @Override
  public int getStatusCode(){
    return 400;
  }
}
