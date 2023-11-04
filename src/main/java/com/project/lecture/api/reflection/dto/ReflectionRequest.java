package com.project.lecture.api.reflection.dto;

import com.project.lecture.type.valid.ValidWeekDt;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @ValidWeekDt
    @NotBlank(message = "빈 값은 들어올 수 없습니다.")
    String weekDt;
  }
}
