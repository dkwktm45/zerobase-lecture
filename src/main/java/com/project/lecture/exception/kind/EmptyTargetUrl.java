package com.project.lecture.exception.kind;

import com.project.lecture.exception.SuperException;

public class EmptyTargetUrl extends SuperException {
  private static final String MESSAGE = "비어 있는 반환 url이 있습니다.";

  public EmptyTargetUrl(){
    super(MESSAGE);
  }

  public EmptyTargetUrl(String name){
    super(MESSAGE);
    addValidation(name,MESSAGE);
  }

  @Override
  public int getStatusCode(){
    return 400;
  }
}
