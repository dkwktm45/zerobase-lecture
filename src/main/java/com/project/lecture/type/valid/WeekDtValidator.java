package com.project.lecture.type.valid;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WeekDtValidator implements ConstraintValidator<ValidWeekDt, String> {

  private ValidWeekDt annotation;
  private static final String REGEX = "^\\d{4}-\\d{2}-\\d{2}/\\d{4}-\\d{2}-\\d{2}$";

  @Override
  public void initialize(ValidWeekDt constraintAnnotation) {
    this.annotation = constraintAnnotation;
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    try {
      Pattern pattern = Pattern.compile(REGEX);

      if (!pattern.matcher(value).matches()) {
        return false;
      }

      String[] weekInfo = value.split("/");
      String startDateStr = weekInfo[0];
      String endDateStr = weekInfo[1];

      LocalDate startDate = LocalDate.parse(startDateStr);
      LocalDate endDate = LocalDate.parse(endDateStr);

      if (startDate.isAfter(LocalDate.now())) {
        return false;
      }

      return ChronoUnit.DAYS.between(startDate, endDate) == 7;
    } catch (NumberFormatException e) {
      log.warn("올바른 숫자 형식이 아닙니다.");
      return false;
    } catch (Exception e) {
      log.warn("예상치 못한 오류가 발생했습니다 : " + e.getMessage());
      return false;
    }
  }
}
