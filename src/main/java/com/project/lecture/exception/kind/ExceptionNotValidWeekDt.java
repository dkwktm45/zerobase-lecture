package com.project.lecture.exception.kind;

import com.project.lecture.exception.SuperException;

public class ExceptionNotValidWeekDt extends SuperException {
  private static final String MESSAGE = "옳바르지 않는 주간 날짜입니다.";

  public ExceptionNotValidWeekDt(){
    super(MESSAGE);
  }

  public ExceptionNotValidWeekDt(String name){
    super(MESSAGE);
    addValidation(name,MESSAGE);
  }

  @Override
  public int getStatusCode(){
    return 400;
  }
}
