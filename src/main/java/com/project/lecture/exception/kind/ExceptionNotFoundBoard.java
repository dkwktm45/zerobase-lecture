package com.project.lecture.exception.kind;

import com.project.lecture.exception.SuperException;

public class ExceptionNotFoundBoard extends SuperException {
  private static final String MESSAGE = "존재하지 않는 게시판입니다.";

  public ExceptionNotFoundBoard(){
    super(MESSAGE);
  }

  public ExceptionNotFoundBoard(String name, String message){
    super(MESSAGE);
    addValidation(name,message);
  }

  @Override
  public int getStatusCode(){
    return 400;
  }
}
