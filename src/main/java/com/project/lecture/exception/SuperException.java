package com.project.lecture.exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class SuperException extends RuntimeException {
  private final Map<String, String> validation = new HashMap<>();

  protected SuperException(String message){
    super(message);
  }

  public abstract int getStatusCode();

  public void addValidation(String fieldName, String message){
    validation.put(fieldName,message);
  }

}
