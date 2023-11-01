package com.project.lecture.api.study.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class StudyRequest {

  @NoArgsConstructor
  @AllArgsConstructor
  @Getter
  @Setter
  public static class Create{
    @NotBlank(message = "빈 값은 들어올 수 없습니다.")
    private String studyTitle;
    @NotBlank(message = "빈 값은 들어올 수 없습니다.")
    private String studyContent;
  }

  @NoArgsConstructor
  @AllArgsConstructor
  @Getter
  @Setter
  public static class Change{
    @NotNull(message = "빈 값은 들어올 수 없습니다.")
    private Long id;
    @NotBlank(message = "빈 값은 들어올 수 없습니다.")
    private String studyTitle;
    @NotBlank(message = "빈 값은 들어올 수 없습니다.")
    private String studyContent;
  }
}
