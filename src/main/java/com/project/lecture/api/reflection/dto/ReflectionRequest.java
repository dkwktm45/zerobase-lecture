package com.project.lecture.api.reflection.dto;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReflectionRequest {

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Create {

    @NotBlank(message = "빈 값은 들어올 수 없습니다.")
    String reflectionTitle;

    @NotBlank(message = "빈 값은 들어올 수 없습니다.")
    String reflectionContent;

    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}/\\d{4}-\\d{2}-\\d{2}$")
    @NotBlank(message = "빈 값은 들어올 수 없습니다.")
    String weekDt;

    public boolean validWeek() {
      try{
        String[] weekInfo = this.weekDt.split("/");
        String startDateStr = weekInfo[0];
        String endDateStr = weekInfo[1];

        LocalDate startDate = LocalDate.parse(startDateStr);
        LocalDate endDate = LocalDate.parse(endDateStr);

        if (startDate.isAfter(LocalDate.now())) {
          return false;
        }

        return ChronoUnit.DAYS.between(startDate, endDate) == 7;
      }catch (NumberFormatException e){
        log.warn("올바른 숫자 형식이 아닙니다.");
        return false;
      }catch (Exception e){
        log.warn("예상치 못한 오류가 발생했습니다 : " + e.getMessage());
        return false;
      }
    }
  }
}
