package com.project.lecture.exception.kind;

import com.project.lecture.exception.SuperException;

public class ExceptionEmptyTargetUrl extends SuperException {
  private static final String MESSAGE = "비어 있는 반환 url이 있습니다.";

  public ExceptionEmptyTargetUrl(){
    super(MESSAGE);
  }

  public ExceptionEmptyTargetUrl(String name){
    super(MESSAGE);
    addValidation(name,MESSAGE);
  }

  @Override
  public int getStatusCode(){
    return 400;
  }
}
