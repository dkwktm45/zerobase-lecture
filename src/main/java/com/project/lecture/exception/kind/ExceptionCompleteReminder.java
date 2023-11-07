package com.project.lecture.exception.kind;

import com.project.lecture.exception.SuperException;

public class ExceptionCompleteReminder extends SuperException {
  private static final String MESSAGE = "이미 완료한 리마인드입니다.";

  public ExceptionCompleteReminder(){
    super(MESSAGE);
  }

  public ExceptionCompleteReminder(String name, String message){
    super(MESSAGE);
    addValidation(name,message);
  }

  @Override
  public int getStatusCode(){
    return 400;
  }
}
