package com.project.lecture.exception.kind;

import com.project.lecture.exception.SuperException;

public class ExceptionNotFoundReminder extends SuperException {
  private static final String MESSAGE = "존재하지 않는 리마인드입니다.";

  public ExceptionNotFoundReminder(){
    super(MESSAGE);
  }

  public ExceptionNotFoundReminder(String name, String message){
    super(MESSAGE);
    addValidation(name,message);
  }

  @Override
  public int getStatusCode(){
    return 400;
  }
}
