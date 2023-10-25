package com.project.lecture.type.valid;

import java.util.Arrays;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EnumValidator implements ConstraintValidator<ValidEnum, Enum> {
  private ValidEnum annotation;

  @Override
  public void initialize(ValidEnum constraintAnnotation) {
    this.annotation = constraintAnnotation;
  }

  @Override
  public boolean isValid(Enum value, ConstraintValidatorContext context) {
    Object[] enumValues = this.annotation.enumClass().getEnumConstants();

    if(enumValues == null){
      return false;
    }

    return Arrays.stream(enumValues).anyMatch(it -> it == value);
  }
}