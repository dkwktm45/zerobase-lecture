package com.project.lecture.api.review.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class ReviewRequest {

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class Create {
    @NotBlank(message = "빈 값은 들어올 수 없습니다.")
    private String reviewContent;
    @Min(0) @Max(10)
    private int reviewRating;
    @NotBlank(message = "빈 값은 들어올 수 없습니다.")
    private String createdEmail;
    @NotNull(message = "빈 값은 들어올 수 없습니다.")
    private Long courseId;
  }

}
