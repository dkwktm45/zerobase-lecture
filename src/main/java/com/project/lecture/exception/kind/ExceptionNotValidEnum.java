package com.project.lecture.exception.kind;

import com.project.lecture.exception.SuperException;

public class ExceptionNotValidEnum extends SuperException {
  private static final String MESSAGE = "Enum 타입이 일치하지 않습니다.";

  public ExceptionNotValidEnum(){
    super(MESSAGE);
  }

  public ExceptionNotValidEnum(String name){
    super(MESSAGE);
    addValidation(name,MESSAGE);
  }

  @Override
  public int getStatusCode(){
    return 400;
  }
}
